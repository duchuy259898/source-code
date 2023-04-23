/*
 * DataSchedules.java
 *
 * Created on 2009/04/30, 14:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author takeda
 */
public class DataSchedules extends ArrayList<DataSchedule>{
    
    private Date        scheduleDate;
    private MstShop     shop;
    private Component   opener = null;
    
    /**
     * Creates a new instance of DataSchedules
     */
    public DataSchedules() {
    }

    public Map loadAll(ConnectionWrapper con, Calendar calen) throws SQLException {
    
        Map result = new HashMap();
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL(calen));
        
        while (rs.next()) {
            ArrayList key = new ArrayList();
            key.add(rs.getInt("staff_id"));
            key.add(rs.getInt("target_day"));
            result.put(key, rs.getInt("shift_id"));
        }

        return result;
    }
    
    public Map loadAllDataScheduleDetail(ConnectionWrapper con, Calendar calen) throws SQLException {
    
        Map result = new HashMap();
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQLDataScheduleDetail(calen));
        
        while (rs.next()) {
            ArrayList key = new ArrayList();
            key.add(rs.getInt("staff_id"));
            key.add(rs.getInt("target_day"));
            result.put(key, rs.getInt("shift_id"));
        }

        return result;
    }

    public Map loadDataScheduleDetail(ConnectionWrapper con, Calendar calen) throws SQLException {
    
        Map result = new HashMap();
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectDataScheduleDetailSQL(calen));
        
        while (rs.next()) {
            ArrayList key = new ArrayList();
            key.add(rs.getInt("staff_id"));
            key.add(rs.getInt("target_day"));
            result.put(key, rs.getBoolean("isRecess"));
        }

        return result;
    }
    
    public Map loadRecess(ConnectionWrapper con, Calendar calen) throws SQLException {
    
        Map result = new HashMap();
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectRecessSQL(calen));
        
        while (rs.next()) {
            ArrayList key = new ArrayList();
            key.add(rs.getInt("staff_id"));
            key.add(rs.getInt("target_day"));
            result.put(key, rs.getBoolean("isRecess"));
        }

        return result;
    }

    /**
     * スケジュールを読み込む
     * @param con ConnectionWrapper
     * @return true - 成功
     * @throws java.sql.SQLException SQLException
     */
    public boolean load(ConnectionWrapper con, boolean isAddBlank) throws SQLException {
    
        this.clear();
        
        if(isAddBlank) {
            this.add(new DataSchedule());
        }
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
        
        Integer staffId = null;
        DataSchedule ms = null;
        
        while (rs.next()) {

            if( ms != null && staffId.intValue() == rs.getInt("staff_id")){
            }else{
                if( ms != null ) this.add(ms);
                ms = new DataSchedule();
                ms.setData(rs);
                staffId = ms.getStaffId();
            }
            
            DataRecess recess = new DataRecess();
            if( recess.setData(rs) ){
                ms.getRecesses().add(recess);
            }
        }

        if( ms != null ) this.add(ms);
        
        return true;
    }

    private String getSelectSQL() {
        return getSelectSQL(null);
    }
    //ptquang
    private String getSelectSQLDataScheduleDetail(Calendar calen) {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        //sql.append("      ds.*");
        sql.append(" ds.shop_id,ds.staff_id,ds.schedule_date,ds.shift_id,ds.insert_date,ds.update_date,ds.delete_date");
        
        sql.append("     ,ms.shift_name");
        sql.append("     ,dr.start_time");
        sql.append("     ,dr.end_time");
        sql.append("     ,ds.start_time as schedule_start");
        sql.append("     ,ds.end_time as schedule_end");
        sql.append("     ,to_char(ds.schedule_date, 'FMDD') as target_day");
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
        sql.append("         left join data_schedule_detail dr");
        sql.append("                on ds.staff_id = dr.staff_id");
        sql.append("               and ds.schedule_date = dr.schedule_date");
        //Luc start add 20140527 Bug #24287 [gb]DB上のスタッフシフト情報と画面表示の不一致
        sql.append("                AND dr.delete_date IS NULL");
        //Luc end add 20140527 Bug #24287 [gb]DB上のスタッフシフト情報と画面表示の不一致
        sql.append("         left join mst_staff mstf");
        sql.append("                on mstf.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("               and mstf.staff_id = ds.staff_id");
        sql.append(" where");
        sql.append("         ds.delete_date is null");
        sql.append("     and ms.delete_date is null");
        //Luc start delete 20150527 Bug #24287 [gb]DB上のスタッフシフト情報と画面表示の不一致
        //sql.append("     and dr.delete_date is null");
        //Luc end delete 20150527 Bug #24287 [gb]DB上のスタッフシフト情報と画面表示の不一致
        // 日付条件
        if (calen != null) {
            
            // 月初～月末
            calen.set(Calendar.DAY_OF_MONTH, 1);
            sql.append(" and ds.schedule_date >= " + SQLUtil.convertForSQLDateOnly(calen.getTime()));
            calen.set(Calendar.DAY_OF_MONTH, calen.getActualMaximum(Calendar.DAY_OF_MONTH));
            sql.append(" and ds.schedule_date <= " + SQLUtil.convertForSQLDateOnly(calen.getTime()));
            
        } else {
            
            // 指定日
            sql.append(" and ds.schedule_date = " + SQLUtil.convertForSQLDateOnly(this.getScheduleDate()));
        }
        
        sql.append("     and ds.staff_id in");
        sql.append("         (");
        sql.append("             select");
        sql.append("                 staff_id");
        sql.append("             from");
        sql.append("                 mst_staff");
        sql.append("             where");
        sql.append("                     delete_date is null");
        
        if (this.getOpener() instanceof RegistShopEmployeePanel) {
            sql.append("             and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        }

        sql.append("         )");
        sql.append(" order by");
        sql.append("      mstf.display_seq");
        sql.append("     ,mstf.staff_no");
        sql.append("     ,ds.schedule_date");
        
        return sql.toString();

    }
    
    private String getSelectSQL(Calendar calen) {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        //sql.append("      ds.*");
        sql.append(" ds.shop_id,ds.staff_id,ds.schedule_date,ds.shift_id,ds.insert_date,ds.update_date,ds.delete_date");
        
        sql.append("     ,ms.shift_name");
        sql.append("     ,dr.recess_id");
        sql.append("     ,dr.start_time");
        sql.append("     ,dr.end_time");
        sql.append("     ,ds.start_time as schedule_start");
        sql.append("     ,ds.end_time as schedule_end");
        sql.append("     ,dr.note");
        sql.append("     ,to_char(ds.schedule_date, 'FMDD') as target_day");
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
        //Luc start add 20140527 Bug #24287 [gb]DB上のスタッフシフト情報と画面表示の不一致
        sql.append("                AND dr.delete_date IS NULL");
        //Luc end add 20140527 Bug #24287 [gb]DB上のスタッフシフト情報と画面表示の不一致
        sql.append("         left join mst_staff mstf");
        sql.append("                on mstf.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("               and mstf.staff_id = ds.staff_id");
        sql.append(" where");
        sql.append("         ds.delete_date is null");
        sql.append("     and ms.delete_date is null");
        //Luc start delete 20150527 Bug #24287 [gb]DB上のスタッフシフト情報と画面表示の不一致
        //sql.append("     and dr.delete_date is null");
        //Luc end delete 20150527 Bug #24287 [gb]DB上のスタッフシフト情報と画面表示の不一致
        // 日付条件
        if (calen != null) {
            
            // 月初～月末
            calen.set(Calendar.DAY_OF_MONTH, 1);
            sql.append(" and ds.schedule_date >= " + SQLUtil.convertForSQLDateOnly(calen.getTime()));
            calen.set(Calendar.DAY_OF_MONTH, calen.getActualMaximum(Calendar.DAY_OF_MONTH));
            sql.append(" and ds.schedule_date <= " + SQLUtil.convertForSQLDateOnly(calen.getTime()));
            
        } else {
            
            // 指定日
            sql.append(" and ds.schedule_date = " + SQLUtil.convertForSQLDateOnly(this.getScheduleDate()));
        }
        
        sql.append("     and ds.staff_id in");
        sql.append("         (");
        sql.append("             select");
        sql.append("                 staff_id");
        sql.append("             from");
        sql.append("                 mst_staff");
        sql.append("             where");
        sql.append("                     delete_date is null");
        
        if (this.getOpener() instanceof RegistRecessPanel) {
            sql.append("             and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        }

        sql.append("         )");
        sql.append(" order by");
        sql.append("      mstf.display_seq");
        sql.append("     ,mstf.staff_no");
        sql.append("     ,ds.schedule_date");
        sql.append("     ,dr.recess_id");
        
        return sql.toString();

    }

    /*
     * スケジュールと休憩データを一括登録する
     */
    public void regist(ConnectionWrapper con) throws SQLException {
        for( DataSchedule sched : this ){
            sched.regist(con);
            for( DataRecess recess : sched.getRecesses() ){
                recess.regist(con);
            }
        }
    }
    
    /*
     * スケジュールのみ一括登録
     */
    public void registSchedule(ConnectionWrapper con) throws SQLException {
        for( DataSchedule sched : this ){
            sched.regist(con);
        }
    }
    
    /*
     * 休憩時間のみ登録
     */
    public void registRecesses(ConnectionWrapper con) throws SQLException {
        
        for( DataSchedule sched : this ){
            sched.getRecesses().regist(con);
        }
    }
    
    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public MstShop getShop() {
        return shop;
    }

    public void setShop(MstShop shop) {
        this.shop = shop;
    }
    
    public Component getOpener() {
        return opener;
    }

    public void setOpener(Component opener) {
        this.opener = opener;
    }

    // 読み込んでいるスケジュールデータより指定のスタッフIDのデータを取り出す
     public DataSchedule getSchedule(Integer staffId){
        DataSchedule ds = null;
        DataRecesses drs = new DataRecesses();
        for( DataSchedule schedule : this ){
            if( schedule.getStaffId().equals(staffId)){
                ds = schedule;
                if(schedule.getRecesses().size() >0){
                    for(DataRecess recess:schedule.getRecesses()) {
                        drs.add(recess);
                    }
                }
                
                
            }
        }
        
        if(ds!= null){
            ds.setRecesses(drs);
        }
      
        return ds;
        
    }
    

    
     /**
     * スケジュールデータにデータが存在するかチェックする。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public static ArrayList<String> getAllSchedulesAndComingYearInYM(ConnectionWrapper con, Integer intShopID) throws SQLException {        
        ArrayList<String> lstYMSchedules = new ArrayList<String>();
        
        if(con == null)	return	lstYMSchedules;

        ResultSetWrapper	rs	=	con.executeQuery(getAllSchedulesAndComingYearInYMSQL(intShopID));
        
        while (rs.next())
        {
            lstYMSchedules.add(rs.getString("schedule_ym"));
        }

        return lstYMSchedules;
    }

   /**
     * YYYYMM形式での全スケジュール取得用 SQL
     */
    private static String getAllSchedulesAndComingYearInYMSQL(Integer intShopID) {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select distinct");
        sql.append("     to_char(schedule_date, 'YYYYMM') as schedule_ym");
        sql.append(" from");
        sql.append("     data_schedule");
        sql.append(" where");
        sql.append("         delete_date is NULL");
        sql.append("     and staff_id in");
        sql.append("         (");
        sql.append("             select staff_id");
        sql.append("               from mst_staff");
        sql.append("              where delete_date is NULL");
        sql.append("                and shop_id = " + SQLUtil.convertForSQL(intShopID));
        sql.append("         )");
        sql.append("     and schedule_date::text >= to_char(current_timestamp::date - '3 months'::interval, 'YYYY-MM-01')");
        for (int i = 0; i < 12; i++) {
            sql.append(" union");
            sql.append(" select to_char(current_timestamp::date + '" + i + " months'::interval, 'YYYYMM')");
        }
//        sql.append(" union");
//        sql.append(" select to_char(current_timestamp::date + '1 months'::interval, 'YYYYMM')");
//        sql.append(" union");
//        sql.append(" select to_char(current_timestamp::date + '2 months'::interval, 'YYYYMM')");
//        sql.append(" union");
//        sql.append(" select to_char(current_timestamp::date + '3 months'::interval, 'YYYYMM')");

        sql.append(" union");
        sql.append(" select to_char(current_timestamp::date - '1 months'::interval, 'YYYYMM')");
        sql.append(" union");
        sql.append(" select to_char(current_timestamp::date - '2 months'::interval, 'YYYYMM')");
        sql.append(" union");
        sql.append(" select to_char(current_timestamp::date - '3 months'::interval, 'YYYYMM')");
        sql.append(" ");
        sql.append(" order by");
        sql.append("     schedule_ym asc");

        return sql.toString();
    }

    private String getSelectRecessSQL(Calendar calen) {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      staff_id");
        sql.append("     ,to_char(schedule_date, 'fmdd') as target_day");
        sql.append("     ,length(max(trim(start_time))) > 0 and length(max(trim(end_time))) > 0 as isRecess");
        sql.append(" from");
        sql.append("     data_recess");
        sql.append(" where");
        sql.append("     delete_date is null");

        // 日付条件
        if (calen != null) {
            // 月初～月末
            calen.set(Calendar.DAY_OF_MONTH, 1);
            sql.append(" and schedule_date >= " + SQLUtil.convertForSQLDateOnly(calen.getTime()));
            calen.set(Calendar.DAY_OF_MONTH, calen.getActualMaximum(Calendar.DAY_OF_MONTH));
            sql.append(" and schedule_date <= " + SQLUtil.convertForSQLDateOnly(calen.getTime()));
        }
        
        sql.append(" group by");
        sql.append("      staff_id");
        sql.append("     ,schedule_date");

        return sql.toString();
    }
    
    private String getSelectDataScheduleDetailSQL(Calendar calen) {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      staff_id");
        sql.append("     ,to_char(schedule_date, 'fmdd') as target_day");
        sql.append("     ,true as isRecess");
        sql.append(" from");
        sql.append("     data_schedule_detail");
        sql.append(" where");
        sql.append("     delete_date is null");

        // 日付条件
        if (calen != null) {
            // 月初～月末
            calen.set(Calendar.DAY_OF_MONTH, 1);
            sql.append(" and schedule_date >= " + SQLUtil.convertForSQLDateOnly(calen.getTime()));
            calen.set(Calendar.DAY_OF_MONTH, calen.getActualMaximum(Calendar.DAY_OF_MONTH));
            sql.append(" and schedule_date <= " + SQLUtil.convertForSQLDateOnly(calen.getTime()));
        }
        
        sql.append(" group by");
        sql.append("      staff_id");
        sql.append("     ,schedule_date");

        return sql.toString();
    }
    
    public void registScheduleDetail(ConnectionWrapper con) throws SQLException {
        
        for( DataSchedule sched : this ){
            sched.getScheduleDetails().regist(con);
        }
    }
    
    public boolean loadCheduleDetail(ConnectionWrapper con) throws SQLException {
    
        this.clear();
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectsCheduleDetailSQL());
        
        DataSchedule ms = null;
        
        while (rs.next()) {

            ms = new DataSchedule();
            ms.setData(rs);
            
            DataScheduleDetail dsd = new DataScheduleDetail();
            if( dsd.setData(rs) ){
                ms.setScheduleDetails(dsd);
            }
            if( ms != null ) this.add(ms);
        }
        
        return true;
    }
    
    private String getSelectsCheduleDetailSQL() {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      dsd.staff_id as staff_id_detail, dsd.schedule_date as schedule_date_detail, dsd.schedule_detail_no,");
        sql.append("      dsd.shop_id as shop_id_detail, dsd.start_time as start_time_detail, dsd.end_time as end_time_detail,");
        sql.append("      dsd.travel_time1 as travel_time1_detail, dsd.travel_time2 as travel_time2_detail, dsd.ext_start_time as ext_start_time_detail, dsd.ext_end_time as ext_end_time_detail");
        sql.append("     ,ds.shop_id,ds.staff_id,ds.schedule_date,ds.shift_id,ds.insert_date,ds.update_date,ds.delete_date");
        sql.append("     ,ms.shift_name");
        sql.append("     ,ds.start_time as schedule_start");
        sql.append("     ,ds.end_time as schedule_end");
        sql.append("     ,mstf.staff_name1");
        sql.append("     ,mstf.staff_name2");
        sql.append("     ,to_char(ds.schedule_date, 'FMDD') as target_day");
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
        sql.append("         left join data_schedule_detail dsd");
        sql.append("                on ds.staff_id = dsd.staff_id");
        sql.append("               and ds.schedule_date = dsd.schedule_date");
        sql.append("                AND dsd.delete_date IS NULL");
        sql.append("         left join mst_staff mstf");
        sql.append("                on mstf.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("               and mstf.staff_id = ds.staff_id");
        sql.append(" where");
        sql.append("         ds.delete_date is null");
        sql.append("     and ms.delete_date is null");
        sql.append(" and ds.schedule_date = " + SQLUtil.convertForSQLDateOnly(this.getScheduleDate()));
        sql.append("     and ds.staff_id in");
        sql.append("         (");
        sql.append("             select");
        sql.append("                 staff_id");
        sql.append("             from");
        sql.append("                 mst_staff");
        sql.append("             where");
        sql.append("                     delete_date is null");
        sql.append("             and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("         )");
        sql.append(" order by");
        sql.append("      mstf.display_seq");
        sql.append("     ,mstf.staff_no");
        sql.append("     ,dsd.start_time");
        
        return sql.toString();
    }
    
    //IVS_LVTu start add 2016/11/17 New request #58626
     /**
     * 既に登録されている休憩が、シフト時間外に存在するかどうかチェックする。
     * @param con ConnectionWrapper
     * @param staffID スタッフID
     * @param day 日付
     * @param startTime シフトの開始時間
     * @param endTime シフトの終了時間
     * @return true - 存在する
     * @throws SQLException 
     */
    public static boolean checkRecessOverlap(ConnectionWrapper con, Integer staffID, Date day, int startTime, int endTime) throws SQLException {
    
        ResultSetWrapper rs = con.executeQuery(getSelectsRecessByStaffSQL(staffID, day, startTime, endTime));
        
        return rs.next();
    }
    
    /**
     * スタッフ休憩情報取得用 SQL
     * @param staffID スタッフID
     * @param startTime シフトの開始時間
     * @param endTime シフトの終了時間
     * @return SQL文
     */
    private static String getSelectsRecessByStaffSQL(Integer staffID, Date day, int startTime, int endTime) {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select * ");
        sql.append(" from data_recess dr ");
        sql.append(" where dr.schedule_date = ").append(SQLUtil.convertForSQL(day));
        sql.append(" and staff_id =  ").append(SQLUtil.convertForSQL(staffID));
        sql.append(" and (dr.start_time :: integer  < ").append(SQLUtil.convertForSQL(startTime));
        sql.append(" or dr.end_time :: integer  > ").append(SQLUtil.convertForSQL(endTime)).append(")");
        sql.append(" and delete_date is null ");
        
        return sql.toString();
    }
    //IVS_LVTu end add 2016/11/17 New request #58626
}

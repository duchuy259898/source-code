/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.DataTargetResultBean;
import com.geobeck.sosia.pos.hair.report.beans.DataTargetStaffNormalBean;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author ivs
 */
public class DataTargetStaffNormalDAO {

    public boolean insertDataTargetStaffNormal(DataTargetStaffNormalBean Bean) {
        
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        String sql = "";
        boolean result = true;
        sql += " insert into data_target_staff_normal(shop_id,staff_id,year,month ,"
                + "target_technic ,target_item ,"
                + "insert_date ,update_date ,delete_date ,target_course ,target_digestion,"
                + "target_technic_num,"
                + "target_nomination_value,"
                + "target_nomination_num,"
                + "target_new_num,"
                + "target_introduction_num,"
                + "target_item_num,"
                + "target_staff_per_sales,"
                + "target_staff_num,"
                + "target_item_rate,"
                + "target_next_reserve_rate,"
                + "target_reserve_close_rate,"
                + "repert_30_new,"
                + "repert_45_new,"
                + "repert_60_new,"
                + "repert_90_new,"
                + "repert_120_new,"
                + "repert_150_new,"
                + "repert_180_new,"
                + "repert_30_fix,"
                + "repert_45_fix,"
                + "repert_60_fix,"
                + "repert_90_fix,"
                + "repert_120_fix,"
                + "repert_150_fix,"
                + "repert_180_fix"
                + ")  values";
        sql += " (";
        sql += " '" + Bean.getShopId() + "',";
        sql += " '" + Bean.getStaff_id()+ "',";
        sql += " '" + Bean.getYear() + "',";
        sql += " '" + Bean.getMonth() + "',";
        sql += " '" + Bean.getTargetTechnic() + "',";
        sql += " '" + Bean.getTargetItem() + "',";
        sql += " now(),";
        sql += " now(),";
        //sql += " '" + Calendar.getInstance().getTime() + "'";
        sql += "null,";
        sql += "" + SQLUtil.convertForSQL(Bean.getTargetCourse()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getTargetDigestion()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getTarget_technic_num()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getTarget_nomination_value()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getTarget_nomination_num()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getTarget_new_num()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getTarget_introduction_num()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getTarget_item_num()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getTarget_staff_per_sales()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getTarget_staff_num()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getTarget_item_rate()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getTarget_next_reserve_rate()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getTarget_reserve_close_rate()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getRepert_30_new()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getRepert_45_new()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getRepert_60_new()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getRepert_90_new()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getRepert_120_new()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getRepert_150_new()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getRepert_180_new()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getRepert_30_fix()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getRepert_45_fix()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getRepert_60_fix()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getRepert_90_fix()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getRepert_120_fix()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getRepert_150_fix()) + ",";
        sql += "" + SQLUtil.convertForSQL(Bean.getRepert_180_fix()) + "";
        sql += " )";
        try {
            jdbcConnection.execute(sql);
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, "DataTargerResultDAO**Ex:" + e.getMessage());
            try {
                jdbcConnection.rollback();
                result = false;
            } catch (SQLException a) {
            }
        }

        return result;
    }

    public boolean updateDataTargetStaffNormal(DataTargetStaffNormalBean Bean) throws SQLException {
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        ResultSetWrapper rs = new ResultSetWrapper();
        String sql = "";
        boolean result = true;

        sql += " Update data_target_staff_normal ";
        sql += " set ";
        sql += " shop_id = '" + Bean.getShopId() + "',";
        sql += " staff_id = '" + Bean.getStaff_id()+ "',";
        sql += " year =  '" + Bean.getYear() + "',";
        sql += " month = '" + Bean.getMonth() + "',";
        sql += " target_technic = '" + Bean.getTargetTechnic() + "',";
        sql += " target_item = '" + Bean.getTargetItem() + "',";
        sql += " update_date = current_timestamp,";
        sql += " delete_date = null ,";
        sql += " target_course = " + SQLUtil.convertForSQL(Bean.getTargetCourse()) + ",";
        sql += " target_digestion = " + SQLUtil.convertForSQL(Bean.getTargetDigestion())+",";
        sql += " target_technic_num = " + SQLUtil.convertForSQL(Bean.getTarget_technic_num()) + ",";
        sql += " target_nomination_value = " + SQLUtil.convertForSQL(Bean.getTarget_nomination_value()) + ",";
        sql += " target_nomination_num = " + SQLUtil.convertForSQL(Bean.getTarget_nomination_num()) + ",";
        sql += " target_new_num = " + SQLUtil.convertForSQL(Bean.getTarget_new_num()) + ",";
        sql += " target_introduction_num = " + SQLUtil.convertForSQL(Bean.getTarget_introduction_num()) + ",";
        sql += " target_item_num = " + SQLUtil.convertForSQL(Bean.getTarget_item_num()) + ",";
        sql += " target_staff_per_sales = " + SQLUtil.convertForSQL(Bean.getTarget_staff_per_sales()) + ",";
        sql += " target_staff_num = " + SQLUtil.convertForSQL(Bean.getTarget_staff_num()) + ",";
        sql += " target_item_rate = " + SQLUtil.convertForSQL(Bean.getTarget_item_rate()) + ",";
        sql += " target_next_reserve_rate = " + SQLUtil.convertForSQL(Bean.getTarget_next_reserve_rate()) + ",";
        sql += " target_reserve_close_rate = " + SQLUtil.convertForSQL(Bean.getTarget_reserve_close_rate()) + ",";
        sql += " repert_30_new = " + SQLUtil.convertForSQL(Bean.getRepert_30_new()) + ",";
        sql += " repert_45_new = " + SQLUtil.convertForSQL(Bean.getRepert_45_new()) + ",";
        sql += " repert_60_new = " + SQLUtil.convertForSQL(Bean.getRepert_60_new()) + ",";
        sql += " repert_90_new = " + SQLUtil.convertForSQL(Bean.getRepert_90_new()) + ",";
        sql += " repert_120_new = " + SQLUtil.convertForSQL(Bean.getRepert_120_new()) + ",";
        sql += " repert_150_new = " + SQLUtil.convertForSQL(Bean.getRepert_150_new()) + ",";
        sql += " repert_180_new = " + SQLUtil.convertForSQL(Bean.getRepert_180_new()) + ",";
        sql += " repert_30_fix = " + SQLUtil.convertForSQL(Bean.getRepert_30_fix()) + ",";
        sql += " repert_45_fix = " + SQLUtil.convertForSQL(Bean.getRepert_45_fix()) + ",";
        sql += " repert_60_fix = " + SQLUtil.convertForSQL(Bean.getRepert_60_fix()) + ",";
        sql += " repert_90_fix = " + SQLUtil.convertForSQL(Bean.getRepert_90_fix()) + ",";
        sql += " repert_120_fix = " + SQLUtil.convertForSQL(Bean.getRepert_120_fix()) + ",";
        sql += " repert_150_fix = " + SQLUtil.convertForSQL(Bean.getRepert_150_fix()) + ",";
        sql += " repert_180_fix = " + SQLUtil.convertForSQL(Bean.getRepert_180_fix()) + "";
        
        
        sql += " where delete_date is null and shop_id =' " + Bean.getShopId() + "' and staff_id= '"+Bean.getStaff_id()+"' and year = '" + Bean.getYear() + "' and month = '" + Bean.getMonth() + "'";
        try {

            jdbcConnection.execute(sql);

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, "DataTargerResultDAO**Ex:" + e.getMessage());

            jdbcConnection.rollback();
            result = false;
        }

        return result;
    }

    public DataTargetStaffNormalBean getDataTargetStaffNormalByShopIdAndStaffIdAndYearAndMonth(int shopId, int staffId,int year, int month) {
        DataTargetStaffNormalBean Bean = new DataTargetStaffNormalBean();
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        ResultSetWrapper rs = new ResultSetWrapper();
        String sql = "";

        try {
            sql += "select * from data_target_staff_normal where staff_id='"+staffId+"' and shop_id =' " + shopId + "' and year = '" + year + "' and month = '" + month + "'";
            rs = jdbcConnection.executeQuery(sql);
            if (rs.first()) {
                Bean.setShopId(rs.getInt("shop_id"));
                Bean.setYear(rs.getInt("year"));
                Bean.setMonth(rs.getInt("month"));
                
                Bean.setTargetTechnic(rs.getInt("target_technic"));
                Bean.setTargetItem(rs.getInt("target_item"));
                Bean.setInsertDate(rs.getTimestamp("insert_date"));
                Bean.setUpdateDate(rs.getTimestamp("update_date"));
                Bean.setDeleteDate(rs.getTimestamp("delete_date"));
                
                Bean.setTarget_nomination_value(rs.getInt("target_nomination_value"));
                Bean.setTarget_technic_num(rs.getInt("target_technic_num"));
                Bean.setTarget_nomination_num(rs.getInt("target_nomination_num"));
                Bean.setTarget_new_num(rs.getInt("target_new_num"));
                Bean.setTarget_introduction_num(rs.getInt("target_introduction_num"));
                Bean.setTarget_item_num(rs.getInt("target_item_num"));
                Bean.setTarget_staff_per_sales(rs.getInt("target_staff_per_sales"));
                Bean.setTarget_staff_num(rs.getInt("target_staff_num"));
                Bean.setTarget_item_rate(rs.getInt("target_item_rate"));
                Bean.setTarget_next_reserve_rate(rs.getInt("target_next_reserve_rate"));
                Bean.setTarget_reserve_close_rate(rs.getInt("target_reserve_close_rate"));
                Bean.setRepert_30_new(rs.getInt("repert_30_new"));
                Bean.setRepert_45_new(rs.getInt("repert_45_new"));
                Bean.setRepert_60_new(rs.getInt("repert_60_new"));
                Bean.setRepert_90_new(rs.getInt("repert_90_new"));
                Bean.setRepert_120_new(rs.getInt("repert_120_new"));
                Bean.setRepert_150_new(rs.getInt("repert_150_new"));
                Bean.setRepert_180_new(rs.getInt("repert_180_new"));
                Bean.setRepert_30_fix(rs.getInt("repert_30_fix"));
                Bean.setRepert_45_fix(rs.getInt("repert_45_fix"));
                Bean.setRepert_60_fix(rs.getInt("repert_60_fix"));
                Bean.setRepert_90_fix(rs.getInt("repert_90_fix"));
                Bean.setRepert_120_fix(rs.getInt("repert_120_fix"));
                Bean.setRepert_150_fix(rs.getInt("repert_150_fix"));
                Bean.setRepert_180_fix(rs.getInt("repert_180_fix"));
            }
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, "DataTargerResultDAO**getDataTargetResultByShopIdAndYearAndMonth**Ex:" + e.getMessage());
        }

        return Bean;
    }
    
    //An start add 20130320getDataTargetResultByShopIdAndYearAndMonthRaPa
    public DataTargetResultBean getDataTargetResultByShopIdAndYearAndMonthRaPa(int shopId, int year, int month) {
        DataTargetResultBean Bean = new DataTargetResultBean();
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        ResultSetWrapper rs = new ResultSetWrapper();
        String sql = "";
        
        try {
            sql += "select * from data_target_result where shop_id =' " + shopId + "' and year = '" + year + "' and month = '" + month + "'";
            rs = jdbcConnection.executeQuery(sql);
            System.out.println(sql);
            if(rs.first()){
             
               Bean.setShopId(rs.getInt("shop_id"));
                Bean.setYear(rs.getInt("year"));
                Bean.setMonth(rs.getInt("month"));
                Bean.setResultTechnic(rs.getInt("result_technic"));
                Bean.setResultItem(rs.getInt("result_item"));
                Bean.setResultIn(rs.getInt("result_in"));
                Bean.setResultNew(rs.getInt("result_new"));
                Bean.setResultK(rs.getInt("result_k"));
                Bean.setResultSet(rs.getInt("result_set"));
                Bean.setResultS(rs.getInt("result_s"));
                Bean.setResultHd(rs.getInt("result_hd"));
                Bean.setResultP(rs.getInt("result_p"));
                Bean.setResultStp(rs.getInt("result_stp"));
                Bean.setResultTr(rs.getInt("result_tr"));
                Bean.setResultEtc(rs.getInt("result_etc"));
                Bean.setResultCrm(rs.getInt("result_crm"));
                Bean.setResultMon(rs.getInt("result_mon"));
                Bean.setTargetTechnic(rs.getInt("target_technic"));
                Bean.setTargetItem(rs.getInt("target_item"));
                Bean.setTargetIn(rs.getInt("target_in"));
                Bean.setTargetNew(rs.getInt("target_new"));
                Bean.setTargetK(rs.getInt("target_k"));
                Bean.setTargetSet(rs.getInt("target_set"));
                Bean.setTargetS(rs.getInt("target_s"));
                Bean.setTargetHd(rs.getInt("target_hd"));
                Bean.setTargetP(rs.getInt("target_p"));
                Bean.setTargetStp(rs.getInt("target_stp"));
                Bean.setTarget_tr(rs.getInt("target_tr"));
                Bean.setTargetEtc(rs.getInt("target_etc"));
                Bean.setTargetCrm(rs.getInt("target_crm"));
                Bean.setOpenDays(rs.getInt("open_days"));
                Bean.setInsertDate(rs.getTimestamp("insert_date"));
                Bean.setUpdateDate(rs.getTimestamp("update_date"));
                Bean.setDeleteDate(rs.getTimestamp("delete_date"));
                Bean.setTargetCourse(rs.getInt("target_course"));
                Bean.setTargetDigestion(rs.getInt("target_digestion"));
                
            }
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, "DataTargerResultDAO**getDataTargetResultByShopIdAndYearAndMonth**Ex:" + e.getMessage());
        }

        return Bean;
    }
    //An end add 20130320
}

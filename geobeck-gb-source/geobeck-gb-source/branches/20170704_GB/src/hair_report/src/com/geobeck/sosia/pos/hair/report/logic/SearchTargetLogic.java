/*
 * SearchTargetLogic.java
 * Created on 2012/10/30, 9:00
 */
package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.DataTargetResultBean;
import com.geobeck.sosia.pos.hair.report.beans.DataTargetStaffNormalBean;
import com.geobeck.sosia.pos.hair.report.beans.SearchTargetResultBean;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;
import java.util.logging.*;

/**
 *
 * @author ivs
 */
public class SearchTargetLogic {

    public SearchTargetLogic() {
    }

    public SearchTargetResultBean getDataTargetResultByShopIdAndYear(Integer intShopID, Integer intYear) {
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        SearchTargetResultBean results = new SearchTargetResultBean();
        ResultSetWrapper rs = null;
        String sql = "";
        try {
            sql += " select a.month  as åé,b.target_technic as ãZèpîÑè„,b.target_item as è§ïiîÑè„,target_technic+b.target_item as total";
            sql += " from (";
            sql += " select 1 as month";
            sql += " union";
            sql += " select 2 as month";
            sql += " union";
            sql += " select 3 as month";
            sql += " union";
            sql += " select 4 as month";
            sql += " union";
            sql += " select 5 as month";
            sql += " union";
            sql += " select 6 as month";
            sql += " union";
            sql += " select 7 as month";
            sql += " union";
            sql += " select 8 as month";
            sql += " union";
            sql += " select 9 as month";
            sql += " union";
            sql += " select 10 as month";
            sql += " union";
            sql += " select 11 as month";
            sql += " union";
            sql += " select 12 as month";
            sql += " ) a";
            sql += " left join data_target_result b on a.month = b.month";
            sql += " where b.shop_id in (" + intShopID.toString() + ") and b.year = '" + intYear.toString() + "'";
            sql += " and b.delete_date is null ";
            rs = jdbcConnection.executeQuery(sql.toString());
            if(rs.first()){
                do {
                    results.getMonth().add(rs.getInt("åé"));
                    results.getTargetTechnic().add(rs.getInt("ãZèpîÑè„"));
                    results.getTargetItem().add(rs.getInt("è§ïiîÑè„"));
                } while (rs.next());
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, "TargetSettingLogic*** Ex:" + e.getLocalizedMessage(),e);
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, "TargetSettingLogic***Ex:" + e.getLocalizedMessage(),e);

        }
        return results;
    }
     public SearchTargetResultBean getDataTargetStaffNormalByShopIdAndStaffIdAndYear(Integer intShopID,Integer staffId, Integer intYear) {
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        SearchTargetResultBean results = new SearchTargetResultBean();
        ResultSetWrapper rs = null;
        String sql = "";
        try {
            sql += " select a.month  as åé,coalesce(b.target_technic,0) as ãZèpîÑè„,"
                    + "coalesce(b.target_item,0) as è§ïiîÑè„,coalesce(target_course,0) as å_ñÒîÑè„,"
                    + "coalesce(target_digestion,0) as è¡âªîÑè„,target_technic+b.target_item as total,";
            sql += " coalesce(target_nomination_value,0) as target_nomination_value,";
            sql += " coalesce(target_technic_num,0) as target_technic_num,";
            sql += " coalesce(target_nomination_num,0) as target_nomination_num,";
            sql += " coalesce(target_new_num,0) as target_new_num,";
            sql += " coalesce(target_introduction_num,0) as target_introduction_num,";
            sql += " coalesce(target_item_num,0) as target_item_num,";
            sql += " coalesce(target_staff_per_sales,0) as target_staff_per_sales,";
            sql += " coalesce(target_staff_num,0) as target_staff_num,";
            sql += " coalesce(target_item_rate,0) as target_item_rate,";
            sql += " coalesce(target_next_reserve_rate,0) as target_next_reserve_rate,";
            sql += " coalesce(target_reserve_close_rate,0) as target_reserve_close_rate,";
            sql += " coalesce(repert_30_new,0) as repert_30_new,";
            sql += " coalesce(repert_45_new,0) as repert_45_new,";
            sql += " coalesce(repert_60_new,0) as repert_60_new,";
            sql += " coalesce(repert_90_new,0) as repert_90_new,";
            sql += " coalesce(repert_120_new,0) as repert_120_new,";
            sql += " coalesce(repert_150_new,0) as repert_150_new,";
            sql += " coalesce(repert_180_new,0) as repert_180_new,";
            sql += " coalesce(repert_30_fix,0) as repert_30_fix,";
            sql += " coalesce(repert_45_fix,0) as repert_45_fix,";
            sql += " coalesce(repert_60_fix,0) as repert_60_fix,";
            sql += " coalesce(repert_90_fix,0) as repert_90_fix,";
            sql += " coalesce(repert_120_fix,0) as repert_120_fix,";
            sql += " coalesce(repert_150_fix,0) as repert_150_fix,";
            sql += " coalesce(repert_180_fix,0) as repert_180_fix";
            sql += " from (";
            sql += " select 1 as month";
            sql += " union";
            sql += " select 2 as month";
            sql += " union";
            sql += " select 3 as month";
            sql += " union";
            sql += " select 4 as month";
            sql += " union";
            sql += " select 5 as month";
            sql += " union";
            sql += " select 6 as month";
            sql += " union";
            sql += " select 7 as month";
            sql += " union";
            sql += " select 8 as month";
            sql += " union";
            sql += " select 9 as month";
            sql += " union";
            sql += " select 10 as month";
            sql += " union";
            sql += " select 11 as month";
            sql += " union";
            sql += " select 12 as month";
            sql += " ) a";
            sql += " left join data_target_staff_normal b on a.month = b.month";
            sql += " where b.staff_id ='"+staffId+"' and b.shop_id in (" + intShopID.toString() + ") and b.year = '" + intYear.toString() + "'";
            sql += " and b.delete_date is null ";
            rs = jdbcConnection.executeQuery(sql.toString());
            if(rs.first()){
                do {
                    results.getMonth().add(rs.getInt("åé"));
                    results.getTargetTechnic().add(rs.getInt("ãZèpîÑè„"));
                    results.getTargetItem().add(rs.getInt("è§ïiîÑè„"));
                    results.getTargetCourse().add(rs.getInt("å_ñÒîÑè„"));
                    results.getTargetDigestion().add(rs.getInt("è¡âªîÑè„"));
                    results.getTarget_technic_num().add(rs.getInt("target_technic_num"));
                    results.getTarget_nomination_value().add(rs.getInt("target_nomination_value"));
                    results.getTarget_nomination_num().add(rs.getInt("target_nomination_num"));
                    results.getTarget_new_num().add(rs.getInt("target_new_num"));
                    results.getTarget_introduction_num().add(rs.getInt("target_introduction_num"));
                    results.getTarget_item_num().add(rs.getInt("target_item_num"));
                    results.getTarget_staff_per_sales().add(rs.getInt("target_staff_per_sales"));
                    results.getTarget_staff_num().add(rs.getInt("target_staff_num"));
                    results.getTarget_item_rate().add(rs.getInt("target_item_rate"));
                    results.getTarget_next_reserve_rate().add(rs.getInt("target_next_reserve_rate"));
                    results.getTarget_reserve_close_rate().add(rs.getInt("target_reserve_close_rate"));
                    results.getRepert_30_new().add(rs.getInt("repert_30_new"));
                    results.getRepert_45_new().add(rs.getInt("repert_45_new"));
                    results.getRepert_60_new().add(rs.getInt("repert_60_new"));
                    results.getRepert_90_new().add(rs.getInt("repert_90_new"));
                    results.getRepert_120_new().add(rs.getInt("repert_120_new"));
                    results.getRepert_150_new().add(rs.getInt("repert_150_new"));
                    results.getRepert_180_new().add(rs.getInt("repert_180_new"));
                    results.getRepert_30_fix().add(rs.getInt("repert_30_fix"));
                    results.getRepert_45_fix().add(rs.getInt("repert_45_fix"));
                    results.getRepert_60_fix().add(rs.getInt("repert_60_fix"));
                    results.getRepert_90_fix().add(rs.getInt("repert_90_fix"));
                    results.getRepert_120_fix().add(rs.getInt("repert_120_fix"));
                    results.getRepert_150_fix().add(rs.getInt("repert_150_fix"));
                    results.getRepert_180_fix().add(rs.getInt("repert_180_fix"));

                } while (rs.next());
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, "TargetSettingLogic*** Ex:" + e.getLocalizedMessage(),e);
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, "TargetSettingLogic***Ex:" + e.getLocalizedMessage(),e);

        }
        return results;
    }
     public SearchTargetResultBean getDataTargetResultByShopIdAndYearRapa(Integer intShopID, Integer intYear) {
        ConnectionWrapper jdbcConnection = SystemInfo.getConnection();
        SearchTargetResultBean results = new SearchTargetResultBean();
        ResultSetWrapper rs = null;
        String sql = "";
        try {
            sql += " select a.month  as åé,coalesce(b.target_technic,0) as ãZèpîÑè„,"
                    + "coalesce(b.target_item,0) as è§ïiîÑè„,coalesce(target_course,0) as å_ñÒîÑè„,"
                    + "coalesce(target_digestion,0) as è¡âªîÑè„,target_technic+b.target_item as total,";
            sql += " coalesce(target_nomination_value,0) as target_nomination_value,";
            sql += " coalesce(target_technic_num,0) as target_technic_num,";
            sql += " coalesce(target_nomination_num,0) as target_nomination_num,";
            sql += " coalesce(target_new_num,0) as target_new_num,";
            sql += " coalesce(target_introduction_num,0) as target_introduction_num,";
            sql += " coalesce(target_item_num,0) as target_item_num,";
            sql += " coalesce(target_staff_per_sales,0) as target_staff_per_sales,";
            sql += " coalesce(target_staff_num,0) as target_staff_num,";
            sql += " coalesce(target_item_rate,0) as target_item_rate,";
            sql += " coalesce(target_next_reserve_rate,0) as target_next_reserve_rate,";
            sql += " coalesce(target_reserve_close_rate,0) as target_reserve_close_rate,";
            sql += " coalesce(repert_30_new,0) as repert_30_new,";
            sql += " coalesce(repert_45_new,0) as repert_45_new,";
            sql += " coalesce(repert_60_new,0) as repert_60_new,";
            sql += " coalesce(repert_90_new,0) as repert_90_new,";
            sql += " coalesce(repert_120_new,0) as repert_120_new,";
            sql += " coalesce(repert_150_new,0) as repert_150_new,";
            sql += " coalesce(repert_180_new,0) as repert_180_new,";
            sql += " coalesce(repert_30_fix,0) as repert_30_fix,";
            sql += " coalesce(repert_45_fix,0) as repert_45_fix,";
            sql += " coalesce(repert_60_fix,0) as repert_60_fix,";
            sql += " coalesce(repert_90_fix,0) as repert_90_fix,";
            sql += " coalesce(repert_120_fix,0) as repert_120_fix,";
            sql += " coalesce(repert_150_fix,0) as repert_150_fix,";
            sql += " coalesce(repert_180_fix,0) as repert_180_fix";
            sql += " from (";
            sql += " select 1 as month";
            sql += " union";
            sql += " select 2 as month";
            sql += " union";
            sql += " select 3 as month";
            sql += " union";
            sql += " select 4 as month";
            sql += " union";
            sql += " select 5 as month";
            sql += " union";
            sql += " select 6 as month";
            sql += " union";
            sql += " select 7 as month";
            sql += " union";
            sql += " select 8 as month";
            sql += " union";
            sql += " select 9 as month";
            sql += " union";
            sql += " select 10 as month";
            sql += " union";
            sql += " select 11 as month";
            sql += " union";
            sql += " select 12 as month";
            sql += " ) a";
            sql += " left join data_target_result b on a.month = b.month";
            sql += " where b.shop_id in (" + intShopID.toString() + ") and b.year = '" + intYear.toString() + "'";
            sql += " and b.delete_date is null ";
            rs = jdbcConnection.executeQuery(sql.toString());
            if(rs.first()){
                do {
                    results.getMonth().add(rs.getInt("åé"));
                    results.getTargetTechnic().add(rs.getInt("ãZèpîÑè„"));
                    results.getTargetItem().add(rs.getInt("è§ïiîÑè„"));
                    results.getTargetCourse().add(rs.getInt("å_ñÒîÑè„"));
                    results.getTargetDigestion().add(rs.getInt("è¡âªîÑè„"));
                    results.getTarget_technic_num().add(rs.getInt("target_technic_num"));
                    results.getTarget_nomination_value().add(rs.getInt("target_nomination_value"));
                    results.getTarget_nomination_num().add(rs.getInt("target_nomination_num"));
                    results.getTarget_new_num().add(rs.getInt("target_new_num"));
                    results.getTarget_introduction_num().add(rs.getInt("target_introduction_num"));
                    results.getTarget_item_num().add(rs.getInt("target_item_num"));
                    results.getTarget_staff_per_sales().add(rs.getInt("target_staff_per_sales"));
                    results.getTarget_staff_num().add(rs.getInt("target_staff_num"));
                    results.getTarget_item_rate().add(rs.getInt("target_item_rate"));
                    results.getTarget_next_reserve_rate().add(rs.getInt("target_next_reserve_rate"));
                    results.getTarget_reserve_close_rate().add(rs.getInt("target_reserve_close_rate"));
                    results.getRepert_30_new().add(rs.getInt("repert_30_new"));
                    results.getRepert_45_new().add(rs.getInt("repert_45_new"));
                    results.getRepert_60_new().add(rs.getInt("repert_60_new"));
                    results.getRepert_90_new().add(rs.getInt("repert_90_new"));
                    results.getRepert_120_new().add(rs.getInt("repert_120_new"));
                    results.getRepert_150_new().add(rs.getInt("repert_150_new"));
                    results.getRepert_180_new().add(rs.getInt("repert_180_new"));
                    results.getRepert_30_fix().add(rs.getInt("repert_30_fix"));
                    results.getRepert_45_fix().add(rs.getInt("repert_45_fix"));
                    results.getRepert_60_fix().add(rs.getInt("repert_60_fix"));
                    results.getRepert_90_fix().add(rs.getInt("repert_90_fix"));
                    results.getRepert_120_fix().add(rs.getInt("repert_120_fix"));
                    results.getRepert_150_fix().add(rs.getInt("repert_150_fix"));
                    results.getRepert_180_fix().add(rs.getInt("repert_180_fix"));

                } while (rs.next());
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, "TargetSettingLogic*** Ex:" + e.getLocalizedMessage(),e);
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, "TargetSettingLogic***Ex:" + e.getLocalizedMessage(),e);

        }
        return results;
    }
    public boolean registDataTargetResult(DataTargetResultBean bean) throws SQLException{
        DataTargerResultDAO DAO = new DataTargerResultDAO();
        DataTargetResultBean t = bean;
        t = DAO.getDataTargetResultByShopIdAndYearAndMonth(bean.getShopId(),bean.getYear(),bean.getMonth());
        boolean result = true;
        if(t != null && t.getShopId()!= 0 && t.getYear() != 0 && t.getMonth() != 0) {
            result = DAO.updateDataTargetResult(bean);
        } else {
            result = DAO.insertDataTargetResult(bean);
        }
        return result;
    } 
     public boolean registDataTargetStaffNormal(DataTargetStaffNormalBean bean) throws SQLException{
        DataTargetStaffNormalDAO DAO = new DataTargetStaffNormalDAO();
        DataTargetStaffNormalBean t = bean;
        t = DAO.getDataTargetStaffNormalByShopIdAndStaffIdAndYearAndMonth(bean.getShopId(),bean.getStaff_id(),bean.getYear(),bean.getMonth());
        boolean result = true;
        if(t != null && t.getShopId()!= 0 && t.getYear() != 0 && t.getMonth() != 0) {
            result = DAO.updateDataTargetStaffNormal(bean);
        } else {
            result = DAO.insertDataTargetStaffNormal(bean);
        }
        return result;
    }   
}

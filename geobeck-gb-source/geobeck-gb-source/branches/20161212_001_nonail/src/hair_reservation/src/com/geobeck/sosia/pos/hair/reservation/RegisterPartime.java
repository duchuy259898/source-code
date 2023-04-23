/*
 * RegisterPartime.java
 *
 * Created on 2016/09/05, 16:39
 */
package com.geobeck.sosia.pos.hair.reservation;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.util.ArrayList;
import java.util.GregorianCalendar;


/**
 * IVS_ptquang add 2016/09/05 New request #54112
 */
public class RegisterPartime implements Cloneable {

    private Integer shop_id_main;
    private Integer shop_id_help;
    private String shopname;
    private GregorianCalendar start_time;
    private GregorianCalendar end_time;
    private String start_time_emp;
    private String end_time_emp;

    /**
     * @return the shopname
     */
    public String getShopname() {
        return shopname;
    }

    /**
     * @param shopname the shopname to set
     */
    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    /**
     * @return the start_time
     */
    public GregorianCalendar getStart_time() {
        return start_time;
    }

    /**
     * @param start_time the start_time to set
     */
    public void setStart_time(GregorianCalendar start_time) {
        this.start_time = start_time;
    }

    /**
     * @return the end_time
     */
    public GregorianCalendar getEnd_time() {
        return end_time;
    }

    /**
     * @param end_time the end_time to set
     */
    public void setEnd_time(GregorianCalendar end_time) {
        this.end_time = end_time;
    }

    public Integer getShop_id_main() {
        return shop_id_main;
    }

    public void setShop_id_main(Integer shop_id_main) {
        this.shop_id_main = shop_id_main;
    }

    public Integer getShop_id_help() {
        return shop_id_help;
    }

    public void setShop_id_help(Integer shop_id_help) {
        this.shop_id_help = shop_id_help;
    }
    /**
     * @return the start_time_emp
     */
    public String getStart_time_emp() {
        return start_time_emp;
    }

    /**
     * @param start_time_emp the start_time_emp to set
     */
    public void setStart_time_emp(String start_time_emp) {
        this.start_time_emp = start_time_emp;
    }

    /**
     * @return the end_time_emp
     */
    public String getEnd_time_emp() {
        return end_time_emp;
    }

    /**
     * @param end_time_emp the end_time_emp to set
     */
    public void setEnd_time_emp(String end_time_emp) {
        this.end_time_emp = end_time_emp;
    }
    /**
     * IVS_ptquang add 2016/09/05 New request #54112
     * @param staff_id
     * @param date
     * @return 
     */
    public String getSelectListStaff(Integer staff_id, GregorianCalendar date) {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" SELECT ");
        sql.append(" mshop.shop_id as main_shop_id, dsd.* ");
        sql.append("  FROM   ");
        sql.append("     data_schedule_detail dsd ");
        sql.append("     INNER JOIN mst_staff mstaff ON dsd.staff_id = mstaff.staff_id  ");
        sql.append("     AND mstaff.delete_date IS NULL  ");
        sql.append("     INNER JOIN mst_shop mshop ON mshop.shop_id = mstaff.shop_id  ");
        sql.append("     WHERE  ");
        sql.append("    dsd.staff_id =" + staff_id);
        sql.append("     AND  ");
        sql.append("     dsd.delete_date IS NULL ");
        sql.append("     AND  ");
        sql.append("    dsd.schedule_date = " + SQLUtil.convertForSQL(date));
        
        return sql.toString();
    }
    /**
     * IVS_ptquang add 2016/09/05 New request #54112
     * @param staff_id
     * @param date
     * @return 
     */
    public ArrayList<RegisterPartime> listDialog(Integer staff_id, GregorianCalendar date,Integer currentShopID) {

        ArrayList<RegisterPartime> list = new ArrayList<RegisterPartime>();
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(this.getSelectListStaff(staff_id, date));
            RegisterPartime part;
            while (rs.next()) {
                part = new RegisterPartime();
                part.shop_id_main = rs.getInt("main_shop_id");
                part.shop_id_help = rs.getInt("shop_id");
                if(currentShopID.equals(part.shop_id_main)) {
                    part.start_time = rs.getGregorianCalendar("ext_start_time");
                    part.end_time = rs.getGregorianCalendar("ext_end_time");
                }else {
                    part.start_time = rs.getGregorianCalendar("start_time");
                    part.end_time = rs.getGregorianCalendar("end_time");
                }
                list.add(part);
            }
        } catch (Exception e) {
        }
        return list;
    }
}

/*
 * MobileMemberData.java
 *
 * Created on 2009/09/08, 10:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import com.geobeck.sql.*;
import java.util.*;
import java.sql.SQLException;

/**
 *
 * @author geobeck
 */
public class MobileMemberData
{
    private GregorianCalendar   addDate =  new GregorianCalendar();
    private Integer             sosiaID = null;
    private String              cusName1 = null;
    private String              cusName2 = null;
    private GregorianCalendar   birthDate = new GregorianCalendar();
    private String              email = null;
    private Integer             sex = null;
    private Boolean             sosiaGear = false;
    private Integer             customerID = null;
    //IVS_LVTu start add 2016/03/10 mmd.getSosiaID()
    private String              fbID        = null;
    
    private String sosiaCode;

    public String getSosiaCode() {
        return sosiaCode;
    }

    public void setSosiaCode(String sosiaCode) {
        this.sosiaCode = sosiaCode;
    }
    
    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }
    //IVS_LVTu end add 2016/03/10 mmd.getSosiaID()
    
    public MobileMemberData()
    {
    }

    public String getAddDateStr() {
        return String.format("%1$tY/%1$tm/%1$td", addDate);
    }

    public void setAddDate(Date addDate) {
        this.addDate.setTime(addDate);
    }

    public Integer getSosiaID() {
        return sosiaID;
    }

    public void setSosiaID(Integer sosiaID) {
        this.sosiaID = sosiaID;
    }

    public String getCusName1() {
        return cusName1;
    }

    public void setCusName1(String cusName1) {
        this.cusName1 = cusName1;
    }

    public String getCusName2() {
        return cusName2;
    }

    public void setCusName2(String cusName2) {
        this.cusName2 = cusName2;
    }

    public String getBirthDateStr() {
        return String.format("%1$tY/%1$tm/%1$td", birthDate);
    }

    public Date getBirthDate() {
        return birthDate.getTime();
    }
    
    public void setBirthDate(Date birthDate) {
        this.birthDate.setTime(birthDate);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Boolean isSosiaGear() {
        return sosiaGear;
    }

    public void setSosiaGear(Boolean sosiaGear) {
        this.sosiaGear = sosiaGear;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }
    
    public void setData(ResultSetWrapper rs) throws SQLException
    {
        this.setAddDate(rs.getDate("add_date"));
        this.setSosiaID(rs.getInt("sosia_id"));
        this.setCusName1(rs.getString("cus_name1"));
        this.setCusName2(rs.getString("cus_name2"));
        this.setBirthDate(rs.getDate("birth_date"));
        this.setEmail(rs.getString("email"));
        this.setSex(rs.getInt("sex"));
        //IVS_LVTu start add 2016/03/10 mmd.getSosiaID()
        this.setFbID(rs.getString("fb_id"));
        //IVS_LVTu end add 2016/03/10 mmd.getSosiaID()
        this.setSosiaCode(rs.getString("sosia_code"));
    }
}

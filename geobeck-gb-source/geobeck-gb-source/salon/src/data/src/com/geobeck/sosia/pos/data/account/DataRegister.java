/*
 * DataRegister.java
 *
 * Created on 2007/03/26, 19:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.account;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.util.*;

/**
 *
 * @author katagiri
 */
public class DataRegister
{
    public static final int MONEY_KINDS		= 10;

    public static final	int MONEY_1		= 0;
    public static final	int MONEY_5		= 1;
    public static final	int MONEY_10		= 2;
    public static final	int MONEY_50		= 3;
    public static final	int MONEY_100		= 4;
    public static final	int MONEY_500		= 5;
    public static final	int MONEY_1000		= 6;
    public static final	int MONEY_2000		= 7;
    public static final	int MONEY_5000		= 8;
    public static final	int MONEY_10000		= 9;

    public static Integer getAmountOfMoney(int index)
    {
        switch(index) {
            case MONEY_1:       return 1;
            case MONEY_5:       return 5;
            case MONEY_10:      return 10;
            case MONEY_50:      return 50;
            case MONEY_100:     return 100;
            case MONEY_500:     return 500;
            case MONEY_1000:    return 1000;
            case MONEY_2000:    return 2000;
            case MONEY_5000:    return 5000;
            case MONEY_10000:   return 10000;
        }

        return 0;
    }

    private MstShop shop = new MstShop();
    private GregorianCalendar manageDate = new GregorianCalendar();

    private Integer baseValue = 0;
    private Vector<Integer> money = new Vector<Integer>();
    private int sendEndFlg = 0;     // 登録＆メール送信フラグ
    private int weatherID = 0;      // 天気ID
    private double staffCount = 0; // スタッフ人数
    private Integer staffId;
    private MstStaff staff;

    public MstStaff getStaff() {
        return staff;
    }

    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }
    

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    private String comment;
    
    /** Creates a new instance of DataRegister */
    public DataRegister()
    {
        this.init();
    }

    private void init()
    {
        for (int i = 0; i < MONEY_KINDS; i ++) {
            money.add(0);
        }
    }

    public MstShop getShop()
    {
        return shop;
    }

    public void setShop(MstShop shop)
    {
        this.shop = shop;
    }

    public Integer getShopID()
    {
        if (shop != null) {
            return shop.getShopID();
        }

        return null;
    }

    public GregorianCalendar getManageDate()
    {
        return manageDate;
    }

    public void setManageDate(GregorianCalendar manageDate)
    {
        this.manageDate = manageDate;
    }


    public Integer getBaseValue()
    {
        return baseValue;
    }

    public void setBaseValue(Integer baseValue)
    {
        this.baseValue = baseValue;
    }

    public Vector<Integer> getMoney()
    {
        return money;
    }

    public void setMoney(Vector<Integer> money)
    {
        this.money = money;
    }

    public Integer getMoney(int index)
    {
        return money.get(index);
    }

    public void setMoney(int index, Integer money)
    {
        this.money.set(index, money);
    }

    public Integer getMoneyValue(int index)
    {
        return this.getAmountOfMoney(index) * money.get(index);
    }

    public void setMoneyValue(int index, Integer value)
    {
        this.money.set(index, value / this.getAmountOfMoney(index));
    }

    public Integer getTotalMoney()
    {
        Integer total = 0;

        for (int i = 0; i < MONEY_KINDS; i ++) {
            total += this.getMoneyValue(i);
        }

        return total;
    }

    public int getWeatherID() {
        return weatherID;
    }

    public void setWeatherID(int weatherID) {
        this.weatherID = weatherID;
    }
    
    public double getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(double staffCount) {
        this.staffCount = staffCount;
    }

    public int getSendEndFlg() {
        return sendEndFlg;
    }

    public void setSendEndFlg(int sendEndFlg) {
        this.sendEndFlg = sendEndFlg;
    }
    
    public void clear()
    {
        this.setBaseValue(0);

        for (int i = 0; i < MONEY_KINDS; i ++) {
            this.setMoney(i, 0);
        }
        
        this.setWeatherID(0);
        this.setStaffCount(0);
        this.setSendEndFlg(0);
    }

    public boolean load(ConnectionWrapper con) throws SQLException
    {
        boolean result = false;

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
        
        if (rs.next()) {
            this.setBaseValue(rs.getInt("base_value"));

            this.setMoney(MONEY_1, rs.getInt("money_1"));
            this.setMoney(MONEY_5, rs.getInt("money_5"));
            this.setMoney(MONEY_10, rs.getInt("money_10"));
            this.setMoney(MONEY_50, rs.getInt("money_50"));
            this.setMoney(MONEY_100, rs.getInt("money_100"));
            this.setMoney(MONEY_500, rs.getInt("money_500"));
            this.setMoney(MONEY_1000, rs.getInt("money_1000"));
            this.setMoney(MONEY_2000, rs.getInt("money_2000"));
            this.setMoney(MONEY_5000, rs.getInt("money_5000"));
            this.setMoney(MONEY_10000, rs.getInt("money_10000"));

            this.setWeatherID(rs.getInt("weather_id"));
            this.setStaffCount(rs.getDouble("staff_count"));
            this.setSendEndFlg(rs.getInt("send_end_flg"));
            //Luc start add 20160314 #48789
            this.setStaffId(rs.getInt("staff_id"));
            this.setComment(rs.getString("comment"));
            //Luc start add 20160314 #48789
            this.setStaff(new MstStaff(staffId));
            this.getStaff().load(con);
            result = true;
        }

        rs.close();

        return result;
    }

    public boolean regist(ConnectionWrapper con) throws SQLException
    {
        if (this.isExist(con)) {
            return (con.executeUpdate(this.getUpdateSQL()) == 1);
        } else {
            return (con.executeUpdate(this.getInsertSQL()) == 1);
        }
    }

    public boolean isExist(ConnectionWrapper con) throws SQLException
    {
        boolean result = false;

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
        result = rs.next();

        rs.close();

        return result;
    }

    private String getSelectSQL()
    {
        return	"select *\n" +
                        "from data_register dr\n" +
                        "where dr.delete_date is null\n" +
                        "and dr.shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
                        "and dr.manage_date = " + SQLUtil.convertForSQLDateOnly(manageDate) + "\n";
    }

    private String getInsertSQL()
    {
        String sql = "insert into data_register(\n" +
                            "shop_id, manage_date, base_value,\n" +
                            "money_1, money_5, money_10, money_50,\n" +
                            "money_100,money_500, money_1000, money_2000,\n" +
                            "money_5000, money_10000,\n" +
                            "weather_id, staff_count,\n" +
                            "insert_date, update_date, delete_date,send_end_flg,staff_id,comment)\n" +
                            "values(\n" +
                            SQLUtil.convertForSQL(this.getShopID()) + ",\n" +
                            SQLUtil.convertForSQLDateOnly(manageDate) + ",\n" +
                            SQLUtil.convertForSQL(this.getBaseValue()) + ",\n";

        for (int i = 0; i < MONEY_KINDS; i ++) {
            sql += SQLUtil.convertForSQL(this.getMoney(i)) + ",\n";
        }

        sql += SQLUtil.convertForSQL(this.getWeatherID()) + ",\n";
        sql += SQLUtil.convertForSQL(this.getStaffCount()) + ",\n";
        
        sql += "current_timestamp, current_timestamp, null,0,";
        //Luc start add 20160314 #48789
        sql += SQLUtil.convertForSQL(this.getStaffId()) + ",\n";
        sql += SQLUtil.convertForSQL(this.getComment()) + "\n";
        //Luc start add 20160314 #48789
        sql += ")\n";

        return sql;
    }

    private String getUpdateSQL()
    {
        return	"update data_register\n" +
                        "set\n" +
                        "base_value = " + SQLUtil.convertForSQL(this.getBaseValue()) + ",\n" +
                        "money_1 = " + SQLUtil.convertForSQL(this.getMoney(MONEY_1)) + ",\n" +
                        "money_5 = " + SQLUtil.convertForSQL(this.getMoney(MONEY_5)) + ",\n" +
                        "money_10 = " + SQLUtil.convertForSQL(this.getMoney(MONEY_10)) + ",\n" +
                        "money_50 = " + SQLUtil.convertForSQL(this.getMoney(MONEY_50)) + ",\n" +
                        "money_100 = " + SQLUtil.convertForSQL(this.getMoney(MONEY_100)) + ",\n" +
                        "money_500 = " + SQLUtil.convertForSQL(this.getMoney(MONEY_500)) + ",\n" +
                        "money_1000 = " + SQLUtil.convertForSQL(this.getMoney(MONEY_1000)) + ",\n" +
                        "money_2000 = " + SQLUtil.convertForSQL(this.getMoney(MONEY_2000)) + ",\n" +
                        "money_5000 = " + SQLUtil.convertForSQL(this.getMoney(MONEY_5000)) + ",\n" +
                        "money_10000 = " + SQLUtil.convertForSQL(this.getMoney(MONEY_10000)) + ",\n" +
                        "weather_id = " + SQLUtil.convertForSQL(this.getWeatherID()) + ",\n" +
                        "staff_count = " + SQLUtil.convertForSQL(this.getStaffCount()) + ",\n" +
                        //Luc start add 20160314 #48789
                        "staff_id = " + SQLUtil.convertForSQL(this.getStaffId()) + ",\n" +
                        "comment = " + SQLUtil.convertForSQL(this.getComment()) + ",\n" +
                        //Luc start add 20160314 #48789
                        "insert_date = case when delete_date is null\n" +
                        "then insert_date else current_timestamp end,\n" +
                        "update_date = current_timestamp,\n" +
                        "delete_date = null\n" +
                        "where shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
                        "and manage_date = " + SQLUtil.convertForSQLDateOnly(manageDate) + "\n";
    }

}

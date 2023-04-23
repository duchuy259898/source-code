/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nahoang GB_mashu 2014/10/06_目標設定
 */
public class MstDataTarget {

    private Integer shopId = null;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    private Integer year = null;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getTechnic() {
        return technic;
    }

    public void setTechnic(Integer technic) {
        this.technic = technic;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Double getFemaleRate() {
        return femaleRate;
    }

    public void setFemaleRate(Double femaleRate) {
        this.femaleRate = femaleRate;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getMaleUnitPrice() {
        return maleUnitPrice;
    }

    public void setMaleUnitPrice(Integer maleUnitPrice) {
        this.maleUnitPrice = maleUnitPrice;
    }

    public Integer getFemaleUnitPirce() {
        return femaleUnitPirce;
    }

    public void setFemaleUnitPirce(Integer femaleUnitPirce) {
        this.femaleUnitPirce = femaleUnitPirce;
    }

    public Integer getCustomer() {
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    public Double getRepert90New() {
        return repeat90New;
    }

    public void setRepert90New(Double repeat90New) {
        this.repeat90New = repeat90New;
    }

    public Double getRepert90Semifix() {
        return repeat90Semifix;
    }

    public void setRepert90Semifix(Double repeat90Semifix) {
        this.repeat90Semifix = repeat90Semifix;
    }

    public Double getRepert90Fix() {
        return repeat90Fix;
    }

    public void setRepert90Fix(Double repeat90Fix) {
        this.repeat90Fix = repeat90Fix;
    }

    public Double getRepert120New() {
        return repeat120New;
    }

    public void setRepert120New(Double repeat120New) {
        this.repeat120New = repeat120New;
    }

    public Double getRepert120Semifix() {
        return repeat120Semifix;
    }

    public void setRepert120Semifix(Double repeat120Semifix) {
        this.repeat120Semifix = repeat120Semifix;
    }

    public Double getRepert120Fix() {
        return repeat120Fix;
    }

    public void setRepert120Fix(Double repeat120Fix) {
        this.repeat120Fix = repeat120Fix;
    }

    public Double getRepert180New() {
        return repeat180New;
    }

    public void setRepert180New(Double repeat180New) {
        this.repeat180New = repeat180New;
    }

    public Double getRepert180Semifix() {
        return repeat180Semifix;
    }

    public void setRepert180Semifix(Double repeat180Semifix) {
        this.repeat180Semifix = repeat180Semifix;
    }

    public Double getRepert180Fix() {
        return repeat180Fix;
    }

    public void setRepert180Fix(Double repeat180Fix) {
        this.repeat180Fix = repeat180Fix;
    }

    public Double getBeforeReserve() {
        return beforeReserve;
    }

    public void setBeforeReserve(Double beforeReserve) {
        this.beforeReserve = beforeReserve;
    }

    public Double getNextReserve() {
        return nextReserve;
    }

    public void setNextReserve(Double nextReserve) {
        this.nextReserve = nextReserve;
    }

    public Double getNewRate() {
        return newRate;
    }

    public void setNewRate(Double newRate) {
        this.newRate = newRate;
    }

    public Integer getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(Integer newCustomer) {
        this.newCustomer = newCustomer;
    }

    public Integer getDecyl1Rate() {
        return decyl1Rate;
    }

    public void setDecyl1Rate(Integer decyl1Rate) {
        this.decyl1Rate = decyl1Rate;
    }

    public Integer getDecyl1Num() {
        return decyl1Num;
    }

    public void setDecyl1Num(Integer decyl1Num) {
        this.decyl1Num = decyl1Num;
    }

    public Integer getDecyl2Rate() {
        return decyl2Rate;
    }

    public void setDecyl2Rate(Integer decyl2Rate) {
        this.decyl2Rate = decyl2Rate;
    }

    public Integer getDecyl2Num() {
        return decyl2Num;
    }

    public void setDecyl2Num(Integer decyl2Num) {
        this.decyl2Num = decyl2Num;
    }

    public Integer getDecyl3Rate() {
        return decyl3Rate;
    }

    public void setDecyl3Rate(Integer decyl3Rate) {
        this.decyl3Rate = decyl3Rate;
    }

    public Integer getDecyl3Num() {
        return decyl3Num;
    }

    public void setDecyl3Num(Integer decyl3Num) {
        this.decyl3Num = decyl3Num;
    }

    public Integer getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(Integer shopCategory) {
        this.shopCategory = shopCategory;
    }

    private Integer month = null;
    private Integer technic = null;
    private Integer item = null;
    private Double femaleRate = null;
    private Integer unitPrice = null;
    private Integer maleUnitPrice = null;
    private Integer femaleUnitPirce = null;
    private Integer customer = null;
    private Double repeat90New = null;
    private Double repeat90Semifix = null;
    private Double repeat90Fix = null;
    private Double repeat120New = null;
    private Double repeat120Semifix = null;
    private Double repeat120Fix = null;
    private Double repeat180New = null;
    private Double repeat180Semifix = null;
    private Double repeat180Fix = null;
    private Double beforeReserve = null;
    private Double nextReserve = null;
    private Double newRate = null;
    private Integer newCustomer = null;
    private Integer decyl1Rate = null;
    private Integer decyl1Num = null;
    private Integer decyl2Rate = null;
    private Integer decyl2Num = null;
    private Integer decyl3Rate = null;
    private Integer decyl3Num = null;
    private Integer shopCategory = null;

    private Integer technicItem = null;
    //IVS_LVTu start add 2015/01/23 Task #35026
    private Double repeat_90_netnew = null;
    private Double repeat_120_netnew = null;
    private Double repeat_180_netnew = null;
    private Double item_sales_rate = null;
    private Integer item_sales_price = null;
    
    private Integer karte1_day      = 0;
    private Integer karte1_value    = 0;
    private Integer karte2_day      = 0;
    private Integer karte2_value    = 0;
    private Integer karte3_day      = 0;
    private Integer karte3_value    = 0;
    private Integer karte4_day      = 0;
    private Integer karte4_value    = 0;
    
    public Integer getKarte1_day() {
        return karte1_day;
    }

    public void setKarte1_day(Integer karte1_day) {
        this.karte1_day = karte1_day;
    }

    public Integer getKarte1_value() {
        return karte1_value;
    }

    public void setKarte1_value(Integer karte1_value) {
        this.karte1_value = karte1_value;
    }

    public Integer getKarte2_day() {
        return karte2_day;
    }

    public void setKarte2_day(Integer karte2_day) {
        this.karte2_day = karte2_day;
    }

    public Integer getKarte2_value() {
        return karte2_value;
    }

    public void setKarte2_value(Integer karte2_value) {
        this.karte2_value = karte2_value;
    }

    public Integer getKarte3_day() {
        return karte3_day;
    }

    public void setKarte3_day(Integer karte3_day) {
        this.karte3_day = karte3_day;
    }

    public Integer getKarte3_value() {
        return karte3_value;
    }

    public void setKarte3_value(Integer karte3_value) {
        this.karte3_value = karte3_value;
    }

    public Integer getKarte4_day() {
        return karte4_day;
    }

    public void setKarte4_day(Integer karte4_day) {
        this.karte4_day = karte4_day;
    }

    public Integer getKarte4_value() {
        return karte4_value;
    }

    public void setKarte4_value(Integer karte4_value) {
        this.karte4_value = karte4_value;
    }
    
    public Double getRepeat_90_netnew() {
        return repeat_90_netnew;
    }

    public void setRepeat_90_netnew(Double repeat_90_netnew) {
        this.repeat_90_netnew = repeat_90_netnew;
    }

    public Double getRepeat_120_netnew() {
        return repeat_120_netnew;
    }

    public void setRepeat_120_netnew(Double repeat_120_netnew) {
        this.repeat_120_netnew = repeat_120_netnew;
    }

    public Double getRepeat_180_netnew() {
        return repeat_180_netnew;
    }

    public void setRepeat_180_netnew(Double repeat_180_netnew) {
        this.repeat_180_netnew = repeat_180_netnew;
    }

    public Double getItem_sales_rate() {
        return item_sales_rate;
    }

    public void setItem_sales_rate(Double item_sales_rate) {
        this.item_sales_rate = item_sales_rate;
    }

    public Integer getItem_sales_price() {
        return item_sales_price;
    }

    public void setItem_sales_price(Integer item_sales_price) {
        this.item_sales_price = item_sales_price;
    }
    //IVS_LVTu end add 2015/01/23 Task #35026

    public Integer getTechnicItem() {
        return technicItem;
    }

    public void setTechnicItem(Integer technicItem) {
        this.technicItem = technicItem;
    }

    public ArrayList<MstDataTarget> getSQLDataTarget(Integer shopId,
            Integer shopCategory, Integer targetYear, Integer targetMonth) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT b.year, b.month,");
        builder.append(" (SELECT technic");
        builder.append("  FROM data_target tg WHERE tg.year = b.year");
        builder.append("    AND tg.month = b.month");
        builder.append("    AND tg.shop_id = " + shopId + "");
        builder.append("    AND tg.shop_category_id=" + shopCategory + ") AS technic_amount,");
        builder.append("  (SELECT item");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS item_amount,");
        builder.append("  (SELECT technic+ item");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS amount,");
        builder.append("  (SELECT customer");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS customer_count,");
        builder.append("  (SELECT new_rate");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS new_customer_rate,");
        builder.append("  (SELECT new_customer");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS new_customer,");
        builder.append("  (SELECT repeat_90_new");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append(" AND tg.shop_category_id=" + shopCategory + ") AS repeat_90_new,");
        builder.append("  (SELECT repeat_90_semifix");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_90_semifix,");
        builder.append("  (SELECT repeat_90_fix");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_90_fix,");
        builder.append("  (SELECT repeat_120_new");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_120_new,");
        builder.append("  (SELECT repeat_120_semifix");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_120_semifix,");
        builder.append("  (SELECT repeat_120_fix");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_120_fix,");
        builder.append("  (SELECT repeat_180_new");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_180_new,");
        builder.append("  (SELECT repeat_180_semifix");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_180_semifix,");
        builder.append("  (SELECT repeat_180_fix");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS repeat_180_fix,");
        builder.append("  (SELECT before_reserve");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS before_reserve,");
        builder.append("  (SELECT next_reserve");
        builder.append("   FROM data_target tg");
        builder.append("   WHERE tg.year =b.year");
        builder.append("     AND tg.month =b.month");
        builder.append("     AND tg.shop_id = " + shopId + "");
        builder.append("     AND tg.shop_category_id=" + shopCategory + ") AS next_reserve");
        builder.append(" FROM");
        builder.append("   (SELECT date_part('year',a.date) AS YEAR,");
        builder.append("           date_part('month',a.date) AS MONTH");
        builder.append("   FROM");
        builder.append("     (SELECT date '" + targetYear + "-" + targetMonth + "-01' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'1 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'2 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'3 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'4 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'5 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'6 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'7 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'8 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'9 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'10 month' AS date");
        builder.append("      UNION SELECT date '" + targetYear + "-" + targetMonth + "-01'+interval'11 month' AS date)a");
        builder.append(" )b");

        ConnectionWrapper con = SystemInfo.getConnection();
        ArrayList<MstDataTarget> listItem = new ArrayList<MstDataTarget>();

        try {
            ResultSetWrapper rs = con.executeQuery(builder.toString());
            while (rs.next()) {
                MstDataTarget targetItem = new MstDataTarget();

                targetItem.setMonth(rs.getInt("month"));
                targetItem.setYear(rs.getInt("year"));
                targetItem.setBeforeReserve(rs.getDouble("before_reserve"));
                targetItem.setCustomer(rs.getInt("customer_count"));
                targetItem.setItem(rs.getInt("item_amount"));
                targetItem.setNewCustomer(rs.getInt("new_customer"));
                targetItem.setNewRate(rs.getDouble("new_customer_rate"));
                targetItem.setNextReserve(rs.getDouble("next_reserve"));
                targetItem.setRepert120Fix(rs.getDouble("repeat_120_fix"));
                targetItem.setRepert120New(rs.getDouble("repeat_120_new"));
                targetItem.setRepert120Semifix(rs.getDouble("repeat_120_semifix"));
                targetItem.setRepert180Fix(rs.getDouble("repeat_180_fix"));
                targetItem.setRepert180New(rs.getDouble("repeat_180_new"));
                targetItem.setRepert180Semifix(rs.getDouble("repeat_180_semifix"));
                targetItem.setRepert90Fix(rs.getDouble("repeat_90_fix"));
                targetItem.setRepert90New(rs.getDouble("repeat_90_new"));
                targetItem.setRepert90Semifix(rs.getDouble("repeat_90_semifix"));
                targetItem.setTechnic(rs.getInt("technic_amount"));
                targetItem.setTechnicItem(rs.getInt("amount"));

                listItem.add(targetItem);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MstDataTarget.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listItem;
    }
    
    //IVS_LVTu start add 2014/10/20 Mashu_設定画面
    
    /**
	 * ベッドマスタにデータを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
            String	sql		=	"";

            if(isExists(con))
            {
                sql	=	this.getUpdateSQL();
            }
            else
            {
                sql	=	this.getInsertSQL();
            }

            if(con.executeUpdate(sql) == 1)
            {
                    return	true;
            }
            else
            {
                    return	false;
            }
	}
        
        /**
	 * ベッドマスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getShopId() == null || this.getYear() == null
                        || this.getMonth()== null|| this.shopCategory == null)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
        
        /**
	 * Selectdata_target
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from data_target\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n" +
				"and	year = " + SQLUtil.convertForSQL(this.getYear()) + "\n"+
                                "and	month = " + SQLUtil.convertForSQL(this.getMonth()) + "\n"+
                                "and	shop_category_id = " + SQLUtil.convertForSQL(this.getShopCategory()) + "\n";
	}
    
    /**
    * ResultSetWrapperからデータをセットする。
    * @param rs ResultSetWrapper
    * @throws java.sql.SQLException SQLException
    */
   public void setData(ResultSetWrapper rs) throws SQLException
   {
       this.setYear(rs.getInt("year"));
       this.setYear(rs.getInt("month"));
       this.setTechnic(rs.getInt("technic_value"));
   }
    
   /**
	 * Insert
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into data_target \n" +
                        "(shop_id, \n" +
                        "	year, \n" +
                        "	month, \n" +
                        "	technic, \n" +
                        "	item, \n" +
                        "	female_rate, \n" +
                        "	unit_price, \n" +
                        "	male_unit_price, \n" +
                        "	female_unit_price, \n" +
                        "	customer, \n" +
                        "	repeat_90_new, \n" +
                        "	repeat_90_semifix, \n" +
                        "	repeat_90_fix, \n" +
                        "	repeat_120_new, \n" +
                        "	repeat_120_semifix, \n" +
                        "	repeat_120_fix, \n" +
                        "	repeat_180_new, \n" +
                        "	repeat_180_semifix, \n" +
                        "	repeat_180_fix, \n" +
                        "	before_reserve, \n" +
                        "	next_reserve, \n" +
                        "	new_rate, \n" +
                        "	new_customer, \n" +
                        "	decyl_1_rate, \n" +
                        "	decyl_1_num, \n" +
                        "	decyl_2_rate, \n" +
                        "	decyl_2_num, \n" +
                        "	decyl_3_rate, \n" +
                        "	decyl_3_num,\n" +
                        "	insert_date,\n" +
                        "	update_date,\n" +
                        "	delete_date,\n" +
                        //IVS_LVTu start add 2015/01/23 Task #35026
                        "	item_sales_rate, \n" +
                        "	item_sales_price,\n" +
                        "	repeat_90_netnew,\n" +
                        "	repeat_120_netnew,\n" +
                        "	repeat_180_netnew,\n" +
                        //IVS_LVTu end add 2015/01/23 Task #35026
                        "       karte1_day,\n" +
                        "       karte2_day,\n" +
                        "       karte3_day,\n" +
                        "       karte4_day,\n" +
                        "       karte1_value,\n" +
                        "       karte2_value,\n" +
                        "       karte3_value,\n" +
                        "       karte4_value,\n" +

                        "	shop_category_id)\n"+
                        "values(" +
                        "	" + SQLUtil.convertForSQL(this.getShopId()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getYear()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getMonth()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getTechnic()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getTechnicItem()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getFemaleRate()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getUnitPrice()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getMaleUnitPrice()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getFemaleUnitPirce()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getCustomer()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getRepert90New()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getRepert90Semifix()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getRepert90Fix()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getRepert120New()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getRepert120Semifix()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getRepert120Fix()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getRepert180New()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getRepert180Semifix()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getRepert180Fix()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getBeforeReserve()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getNextReserve()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getNewRate()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getNewCustomer()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getDecyl1Rate()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getDecyl1Num()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getDecyl2Rate()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getDecyl2Num()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getDecyl3Rate()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getDecyl3Num()) + ", \n" +
                        "	current_timestamp, \n" +
                        "	current_timestamp, \n" +
                        "	null, \n" +
                        //IVS_LVTu start add 2015/01/23 Task #35026
                        "	" + SQLUtil.convertForSQL(this.getItem_sales_rate()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getItem_sales_price()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getRepeat_90_netnew()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getRepeat_120_netnew()) + ", \n" +
                        "	" + SQLUtil.convertForSQL(this.getRepeat_180_netnew()) + ", \n" +
                        //IVS_LVTu end add 2015/01/23 Task #35026
                        
                        "        " + SQLUtil.convertForSQL(this.getKarte1_day())+",\n" +    
                        "        " + SQLUtil.convertForSQL(this.getKarte2_day())+",\n" +       
                        "        " + SQLUtil.convertForSQL(this.getKarte3_day())+",\n" +       
                        "        " + SQLUtil.convertForSQL(this.getKarte4_day())+",\n" +  

                        "        " + SQLUtil.convertForSQL(this.getKarte1_value())+",\n" +    
                        "        " + SQLUtil.convertForSQL(this.getKarte2_value())+",\n" +       
                        "        " + SQLUtil.convertForSQL(this.getKarte3_value())+",\n" +       
                        "        " + SQLUtil.convertForSQL(this.getKarte4_value())+",\n" + 
                        
                        "	" + SQLUtil.convertForSQL(this.getShopCategory()) + ")\n" ;
	}
	
	/**
	 * Update
	 * @return Update
	 */
	private String	getUpdateSQL()
	{
		return	"update data_target \n" +
                "set \n" +
                "	technic               = "+SQLUtil.convertForSQL(this.getTechnic())+",\n" +
                "	item                  = "+SQLUtil.convertForSQL(this.getTechnicItem())+", \n" +
                "	female_rate           = "+SQLUtil.convertForSQL(this.getFemaleRate())+",\n" +
                "	unit_price            = "+SQLUtil.convertForSQL(this.getUnitPrice())+", \n" +
                "	male_unit_price       = "+SQLUtil.convertForSQL(this.getMaleUnitPrice())+",\n" +
                "	female_unit_price     = "+SQLUtil.convertForSQL(this.getFemaleUnitPirce())+",\n" +
                "	customer              = "+SQLUtil.convertForSQL(this.getCustomer())+",\n" +
                "	repeat_90_new         = "+SQLUtil.convertForSQL(this.getRepert90New())+",\n" +
                "	repeat_90_semifix     = "+SQLUtil.convertForSQL(this.getRepert90Semifix())+",\n" +
                "	repeat_90_fix         = "+SQLUtil.convertForSQL(this.getRepert90Fix())+",\n" +
                "	repeat_120_new        = "+SQLUtil.convertForSQL(this.getRepert120New())+",\n" +
                "	repeat_120_semifix    = "+SQLUtil.convertForSQL(this.getRepert120Semifix())+",\n" +
                "	repeat_120_fix        = "+SQLUtil.convertForSQL(this.getRepert120Fix())+",\n" +
                "	repeat_180_new        = "+SQLUtil.convertForSQL(this.getRepert180New())+",\n" +
                "	repeat_180_semifix    = "+SQLUtil.convertForSQL(this.getRepert180Semifix())+",\n" +
                "	repeat_180_fix        = "+SQLUtil.convertForSQL(this.getRepert180Fix())+",\n" +
                "	before_reserve        = "+SQLUtil.convertForSQL(this.getBeforeReserve())+",\n" +
                "	next_reserve          = "+SQLUtil.convertForSQL(this.getNextReserve())+",\n" +
                "	new_rate              = "+SQLUtil.convertForSQL(this.getNewRate())+",\n" +
                "	new_customer          = "+SQLUtil.convertForSQL(this.getNewCustomer())+",\n" +
                "	decyl_1_rate          = "+SQLUtil.convertForSQL(this.getDecyl1Rate())+",\n" +
                "	decyl_1_num           = "+SQLUtil.convertForSQL(this.getDecyl1Num())+",\n" +
                "	decyl_2_rate          = "+SQLUtil.convertForSQL(this.getDecyl2Rate())+",\n" +
                "	decyl_2_num           = "+SQLUtil.convertForSQL(this.getDecyl2Num())+",\n" +
                "	decyl_3_rate          = "+SQLUtil.convertForSQL(this.getDecyl3Rate())+",\n" +
                "	decyl_3_num	      = "+SQLUtil.convertForSQL(this.getDecyl3Num())+",\n" +
                "	update_date	      = current_timestamp , \n" +
                //IVS_LVTu start add 2015/01/23 Task #35026        
                "        item_sales_rate      = "+SQLUtil.convertForSQL(this.getItem_sales_rate())+",\n" +
                "        item_sales_price     = "+SQLUtil.convertForSQL(this.getItem_sales_price())+",\n" +
                "        repeat_90_netnew     = "+SQLUtil.convertForSQL(this.getRepeat_90_netnew())+",\n" +
                "        repeat_120_netnew    = "+SQLUtil.convertForSQL(this.getRepeat_120_netnew())+",\n" +
                "        repeat_180_netnew    = "+SQLUtil.convertForSQL(this.getRepeat_180_netnew())+",\n" +
                //IVS_LVTu end add 2015/01/23 Task #35026        
                        
                "        karte1_day      = "+SQLUtil.convertForSQL(this.getKarte1_day())+",\n" +    
                "        karte2_day      = "+SQLUtil.convertForSQL(this.getKarte2_day())+",\n" +       
                "        karte3_day      = "+SQLUtil.convertForSQL(this.getKarte3_day())+",\n" +       
                "        karte4_day      = "+SQLUtil.convertForSQL(this.getKarte4_day())+",\n" +  
                        
                "        karte1_value      = "+SQLUtil.convertForSQL(this.getKarte1_value())+",\n" +    
                "        karte2_value      = "+SQLUtil.convertForSQL(this.getKarte2_value())+",\n" +       
                "        karte3_value      = "+SQLUtil.convertForSQL(this.getKarte3_value())+",\n" +       
                "        karte4_value      = "+SQLUtil.convertForSQL(this.getKarte4_value())+"\n" +       
                "where \n" +
                "	shop_id = "+SQLUtil.convertForSQL(this.getShopId())+"\n" +
                "	and shop_category_id      = "+SQLUtil.convertForSQL(this.getShopCategory())+"\n" +
                "	and year                  = "+SQLUtil.convertForSQL(this.getYear())+"\n" +
                "	and month                 = "+SQLUtil.convertForSQL(this.getMonth())+"";
	}
        
    //IVS_LVTu end add 2014/10/20 Mashu_設定画面
    
    //IVS_LVTu end add 2015/01/23 Task #35026
    /**
     * get data_target
     * @param con
     * @param shopId
     * @param shopCategoryId
     * @param year
     * @throws SQLException 
     */
     public ArrayList<MstDataTarget>  getSelectTarget(ConnectionWrapper con, Integer shopId, Integer shopCategoryId, String date) throws SQLException
    {
         ArrayList<MstDataTarget> arr = new ArrayList<MstDataTarget>();
        StringBuilder sql = new StringBuilder(1000);
        
        sql.append(" SELECT b.year, ");
        sql.append(" 	   b.month, ");
        sql.append("   coalesce((SELECT item_sales_rate ");
        sql.append("    FROM data_target dt ");
        sql.append("    WHERE dt.shop_id = " + shopId + " ");
        sql.append(" 	 AND dt.shop_category_id = " + shopCategoryId + " ");
        sql.append(" 	 AND dt.year = b.year ");
        sql.append(" 	 AND dt.month = b.month),0) as item_sales_rate, ");
        sql.append("    coalesce((SELECT coalesce(item_sales_price,0)  ");
        sql.append("    FROM data_target dt ");
        sql.append("    WHERE dt.shop_id = " + shopId + " ");
        sql.append(" 	 AND dt.shop_category_id = " + shopCategoryId + " ");
        sql.append(" 	 AND dt.year = b.year ");
        sql.append(" 	 AND dt.month = b.month),0) as item_sales_price ");	 
        sql.append(" FROM ");
        sql.append("   (SELECT date_part('year',a.date) AS YEAR, ");
        sql.append(" 		  date_part('month',a.date) AS MONTH ");
        sql.append("    FROM ");
        sql.append(" 	 (SELECT date'"+ date + "' +interval'0 month'AS date ");
        sql.append(" 	  UNION SELECT date '"+ date + "'+interval'1 month' AS date ");
        sql.append(" 	  UNION SELECT date '"+ date + "'+interval'2 month' AS date ");
        sql.append(" 	  UNION SELECT date '"+ date + "'+interval'3 month' AS date ");
        sql.append(" 	  UNION SELECT date '"+ date + "'+interval'4 month' AS date ");
        sql.append(" 	  UNION SELECT date '"+ date + "'+interval'5 month' AS date ");
        sql.append(" 	  UNION SELECT date '"+ date + "'+interval'6 month' AS date ");
        sql.append(" 	  UNION SELECT date '"+ date + "'+interval'7 month' AS date ");
        sql.append(" 	  UNION SELECT date '"+ date + "'+interval'8 month' AS date ");
        sql.append(" 	  UNION SELECT date '"+ date + "'+interval'9 month' AS date ");
        sql.append(" 	  UNION SELECT date '"+ date + "'+interval'10 month' AS date ");
        sql.append(" 	  UNION SELECT date '"+ date + "'+interval'11 month' AS date)a)b ");
        sql.append(" ORDER BY b.YEAR, b.MONTH ");

        ResultSetWrapper	rs	=	con.executeQuery( sql.toString());
        while(rs.next())
        {
            MstDataTarget mstdt = new MstDataTarget();
            mstdt.item_sales_rate = rs.getDouble("item_sales_rate");
            mstdt.item_sales_price = rs.getInt("item_sales_price");
            arr.add(mstdt);
        }
        rs.close();
        return arr;
    }
    //IVS_LVTu end add 2015/01/23 Task #35026

}

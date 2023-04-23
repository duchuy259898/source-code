/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.master.product;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nahoang IVS_nahoang start add 10/10/2014 GB_Mashu_目標設定
 */
public class DataTargetTechnic {

    private Integer technicValue = null;

    public Integer getTechnicValue() {
        return technicValue;
    }

    public void setTechnicValue(Integer technicValue) {
        this.technicValue = technicValue;
    }

    private Double technicRate = null;

    public Double getTechnicRate() {
        return technicRate;
    }

    public void setTechnicRate(Double technicRate) {
        this.technicRate = technicRate;
    }

    private String technicClassName = "";

    public String getTechnicClassName() {
        return technicClassName;
    }

    public void setTechnicClassName(String technicClassName) {
        this.technicClassName = technicClassName;
    }

    private Integer technicClassID = null;

    public Integer getTechnicClassID() {
        return technicClassID;
    }

    public void setTechnicClassID(Integer technicClassID) {
        this.technicClassID = technicClassID;
    }

    //IVS_LVTu start add 2014/10/20 Mashu_設定画面
    private Integer shopId = 0;
    private Integer year = 0;
    private Integer month = 0;
    private Integer shopCategoryId = 0;
    //IVS_LVTu end add 2014/10/20 Mashu_設定画面

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

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

    public Integer getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(Integer shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }
        
    public ArrayList<DataTargetTechnic> getSQLTechnicTarget(Integer targetYear, Integer targetMonth,
            Integer shopCategory, Integer shopId) {
        StringBuilder builder = new StringBuilder();

        builder.append("        SELECT technic_class_id,");
        builder.append("       technic_class_name ,");
        builder.append("  (SELECT coalesce(tgt.value,0)");
        builder.append("   FROM data_target_technic tgt");
        builder.append("   WHERE tgt.year =" + targetYear + "");
        builder.append("     AND tgt.month =" + targetMonth + "");
        builder.append("     AND tgt.shop_category_id=" + shopCategory + "");
        builder.append("     AND tgt.shop_id IN (" + shopId + ")");
        builder.append("     AND tgt.technic_class_id = mtc.technic_class_id) AS technic_value,");
        builder.append("  (SELECT coalesce(tgt.rate,0)");
        builder.append("   FROM data_target_technic tgt");
        builder.append("   WHERE tgt.year =" + targetYear + "");
        builder.append("     AND tgt.month =" + targetMonth + "");
        builder.append("     AND tgt.shop_category_id=" + shopCategory + "");
        builder.append("     AND tgt.technic_class_id = mtc.technic_class_id");
        builder.append("     AND tgt.shop_id IN (" + shopId + ")) AS technic_rate");
        builder.append(" FROM");
        builder.append("  (SELECT technic_class_id,");
        builder.append("          technic_class_name");
        builder.append("   FROM mst_technic_class mtc1");
        builder.append("   WHERE delete_date IS NULL");
        //IVS_nahoang start add 2014/12/02 Mashu_目標管理画面 Task #33427
        builder.append("        AND prepaid = 0");
        builder.append("  AND EXISTS");
        builder.append("    (SELECT 1");
        builder.append("     FROM mst_use_product mup");
        builder.append("     INNER JOIN mst_technic mt ON mup.product_id = mt.technic_id");
        builder.append("     WHERE mup.shop_id =" + shopId + "");
        builder.append("       AND mup.product_division = 1");
        builder.append("       AND mt.technic_class_id = mtc1.technic_class_id)");

        if (shopCategory != 0) {
            builder.append("  and technic_class_id in (SELECT technic_class_id FROM mst_technic_class ");
            builder.append("  WHERE shop_category_id = " + shopCategory + " ");
            builder.append("    and delete_date is null) ");
        }
        //IVS_nahoang end add 2014/12/02
        builder.append(" order by display_seq) mtc");
               
        ArrayList<DataTargetTechnic> listDataTechnic = new ArrayList<DataTargetTechnic>();
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(builder.toString());
            while (rs.next()) {
                DataTargetTechnic dataTargetTechnic = new DataTargetTechnic();
                dataTargetTechnic.setTechnicClassID(rs.getInt("technic_class_id"));
                dataTargetTechnic.setTechnicClassName(rs.getString("technic_class_name"));
                dataTargetTechnic.setTechnicRate(rs.getDouble("technic_rate"));
                dataTargetTechnic.setTechnicValue(rs.getInt("technic_value"));

                listDataTechnic.add(dataTargetTechnic);
            }

            con.close();
            rs.close();
        } catch (Exception ex) {
            Logger.getLogger(MstDataTarget.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listDataTechnic;
    }
        //IVS_nahoang end add GB_mashu_目標設定
    
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
                   || this.getMonth()== null|| this.getTechnicClassID()== null|| this.getShopCategoryId() == null)	return	false;

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
                           "from data_target_technic\n" +
                           "where	shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n" +
                           "and	year = " + SQLUtil.convertForSQL(this.getYear()) + "\n"+
                           "and	month = " + SQLUtil.convertForSQL(this.getMonth()) + "\n"+
                           "and	shop_category_id = " + SQLUtil.convertForSQL(this.getShopCategoryId()) + "\n"+
                           "and	technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClassID()) + "\n";
   }

   private String	getInsertSQL()
   {
           return	"insert into data_target_technic \n" +
                   "(shop_id, year, month,technic_class_id, rate, value, insert_date, update_date, delete_date ,shop_category_id)  \n" +
                   "	values( \n" +

                   "	" + SQLUtil.convertForSQL(this.getShopId()) + ", \n" +
                   "	" + SQLUtil.convertForSQL(this.getYear()) + ", \n" +
                   "	" + SQLUtil.convertForSQL(this.getMonth()) + ", \n" +
                   "	" + SQLUtil.convertForSQL(this.getTechnicClassID()) + ", \n" +
                   "	" + SQLUtil.convertForSQL(this.getTechnicRate()) + ", \n" +
                   "	" + SQLUtil.convertForSQL(this.getTechnicValue()) + ", \n" +
                   "	current_timestamp, \n" +
                   "	current_timestamp, \n" +
                   "	null, \n" +
                   "	" + SQLUtil.convertForSQL(this.getShopCategoryId()) + ")\n" ;
   }

   /**
    * Update
    * @return Update
    */
   private String	getUpdateSQL()
   {
           return	"update data_target_technic \n" +
           "set \n" +
           "	rate	      = "+SQLUtil.convertForSQL(this.getTechnicRate())+",\n" +
           "	value	      = "+SQLUtil.convertForSQL(this.getTechnicValue())+",\n" +
           "	update_date	          = current_timestamp \n" +
           "where \n" +
           "	shop_id                   = "+SQLUtil.convertForSQL(this.getShopId())+"\n" +
           "	and shop_category_id      = "+SQLUtil.convertForSQL(this.getShopCategoryId())+"\n" +
           "	and technic_class_id      = "+SQLUtil.convertForSQL(this.getTechnicClassID())+"\n" +
           "	and year                  = "+SQLUtil.convertForSQL(this.getYear())+"\n" +
           "	and month                 = "+SQLUtil.convertForSQL(this.getMonth())+"";
   }
    //IVS_LVTu end add 2014/10/20 Mashu_設定画面
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lvtu
 */
public class DataTargets extends ArrayList<DataTarget>{
    
    class SetTargetMenu
    {
        public Integer technicClassId = 0;
        public String technicClassName = "";
        public Integer salesValue  = 0;
        public Integer customerCount  = 0;
        public Integer totalNum = 0;
        public Integer technicTargetRate  = 0;
        public Integer technicTargetPrice  = 0;
    };
    class SetProductTarget
    {
        public Integer itemClassId = 0;
        public String item_class_name = "";
        public Integer salesValue  = 0;
        public Integer itemTargetRate = 0;
    };
    
    private Integer totalValue = 0;
    private Integer trophyCustomerWomenValue = 0;
    private Integer trophyCustomerManValue = 0;    
    
    private ArrayList<DataTargetMotive> arrDataTargetMotive = new ArrayList<DataTargetMotive>();
    //private ArrayList<FirstComingMotive> arrFistComingMotive = new ArrayList<FirstComingMotive>();
    private ArrayList<SetTargetMenu> setTargetMenu = new ArrayList<SetTargetMenu>();
    private ArrayList<SetProductTarget> setProductTarget = new ArrayList<SetProductTarget>();

    public ArrayList<SetProductTarget> getSetProductTarget() {
        return setProductTarget;
    }

    public void setSetProductTarget(ArrayList<SetProductTarget> setProductTarget) {
        this.setProductTarget = setProductTarget;
    }

    public ArrayList<SetTargetMenu> getSetTargetMenu() {
        return setTargetMenu;
    }

    public void setSetTargetMenu(ArrayList<SetTargetMenu> setTargetMenu) {
        this.setTargetMenu = setTargetMenu;
    }

    public ArrayList<DataTargetMotive> getArrDataTargetMotive() {
        return arrDataTargetMotive;
    }

    public void setArrDataTargetMotive(ArrayList<DataTargetMotive> arrDataTargetMotive) {
        this.arrDataTargetMotive = arrDataTargetMotive;
    }

    public Integer getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Integer totalValue) {
        this.totalValue = totalValue;
    }

    public Integer getTrophyCustomerWomenValue() {
        return trophyCustomerWomenValue;
    }

    public void setTrophyCustomerWomenValue(Integer trophyCustomerWomenValue) {
        this.trophyCustomerWomenValue = trophyCustomerWomenValue;
    }

    public Integer getTrophyCustomerManValue() {
        return trophyCustomerManValue;
    }

    public void setTrophyCustomerManValue(Integer trophyCustomerManValue) {
        this.trophyCustomerManValue = trophyCustomerManValue;
    }
            
    /**
    * ResultSetWrapperÇ©ÇÁÉfÅ[É^ÇÉZÉbÉgÇ∑ÇÈÅB
    * @param rs ResultSetWrapper
    * @throws java.sql.SQLException SQLException
    */
   public void setData(ResultSetWrapper rs, int type) throws SQLException
   {
       if ( type == 1)
       {
            this.setTotalValue(Math.round(rs.getFloat("total_value")));
       }
       if( type == 2)
       {
            this.setTrophyCustomerWomenValue(Math.round(rs.getFloat("women_value")));
            this.setTrophyCustomerManValue(Math.round(rs.getFloat("man_value")));
       }
   }
   
   /**
    * ÅÉãZèpîÑè„ñ⁄ïWê›íËÅÑÅEåéäiç∑ÇéQçlÇ…ÇµÇƒñàåéÇäÑÇËêUÇÈÇ∆â∫ãLÇÃÇÊÇ§Ç…Ç»ÇËÇ‹Ç∑ÅB
    * @param con ConnectionWrapper
    * @throws java.sql.SQLException SQLException
    */
    public void loadDataTarget(ConnectionWrapper con, Integer shopId, Integer shopCategoryId, String date, int type) throws SQLException
    {
            ResultSetWrapper	rs	=	con.executeQuery( this.getSelectByShopIdByShopCategoryIdSQL(shopId, shopCategoryId, date, type));

            while(rs.next())
            {
                DataTarget dt = new DataTarget();
                dt.setData(rs);
                this.add(dt);
            }

            rs.close();
    }
    /**
     * ÅÉãZèpîÑè„ñ⁄ïWê›íËÅÑÅEåéäiç∑ÇéQçlÇ…ÇµÇƒñàåéÇäÑÇËêUÇÈÇ∆â∫ãLÇÃÇÊÇ§Ç…Ç»ÇËÇ‹Ç∑ÅB
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param date String
     * @param type 
     * @throws SQLException SQLException
     */
    public void loadDataSales(ConnectionWrapper con, Integer shopId, Integer shopCategoryId, String date, int type) throws SQLException
    {
            ResultSetWrapper	rs	=	con.executeQuery( this.getDataSalesByShopIdSQL(shopId, shopCategoryId, date, type));

            while(rs.next())
            {
                DataTarget dt = new DataTarget();
                dt.setData(rs);
                this.add(dt);
            }

            rs.close();
    }
    /**
     * ÅÉãZèpãqíPâøñ⁄ïWê›íËÅÑ. ëÂÇ‹Ç©Ç»èóê´ãqêîî‰ó¶Çê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢
    * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param year Integer.
     * @param month Integer.
     * @param type Integer.
     * @return Integer
     * @throws SQLException 
     */
    public Integer loadDataCustomer(ConnectionWrapper con, Integer shopId, Integer shopCategoryId, Integer year, int periodMonth) throws SQLException
    {
        StringBuilder sql = new StringBuilder(1000);
        //IVS_LVTu start edit 2014/10/29 Mashu_ê›íËâÊñ 
        sql.append("  (SELECT " );
        sql.append("	 (SELECT coalesce(count(DISTINCT vdsd.slip_no),0) " );
        sql.append("	  FROM view_data_sales_detail_valid vdsd " );
        sql.append("	  INNER JOIN mst_technic mt ON vdsd.product_id = mt.technic_id " );
        sql.append("	  INNER JOIN mst_technic_class mtc ON mtc.technic_class_id = mt.technic_class_id " );
        //sql.append("	  INNER JOIN mst_customer mc ON mc.customer_id = vdsd.customer_id " );
        sql.append("	  INNER JOIN mst_customer mc ON mc.customer_id = vdsd.customer_id  AND mc.customer_no <> '0' " );
        sql.append("	  WHERE vdsd.shop_id ="+shopId+" " );
        sql.append("		AND product_division = 1 " );
        if(shopCategoryId != 0)
        {
            sql.append("		AND mtc.shop_category_id = "+shopCategoryId+" " );
        }
        // type = 1 when periodMonth = 12 , type = 2 when periodMonth = 3
        if(periodMonth == 12)
        {
            sql.append("		AND date_part('year',sales_date) = "+year+"-1  " );
        }
        else
        {
            sql.append("		AND ((date_part('year',sales_date) = "+year+"-1  " );
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            sql.append("		AND date_part('month',sales_date) > " + periodMonth + ") or (date_part('year',sales_date) = "+year+" " );
            sql.append("		AND date_part('month',sales_date) <=" + periodMonth + ")) " );
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        }
        sql.append("	) as count)  ");

        //IVS_LVTu end edit 2014/10/29 Mashu_ê›íËâÊñ 
        ResultSetWrapper	rs1	=	con.executeQuery( sql.toString());
        int results = 0;
        if(rs1.next())
        {
            Integer count = rs1.getInt("count");
            if(count > 0)
            {
                ResultSetWrapper	rs	=	con.executeQuery( this.getDataCustomerSQL(shopId, shopCategoryId, year, periodMonth));
                while(rs.next())
                {
                    //results = rs.getInt("customer_rate");
                    Double value = 0d;
                    value = rs.getDouble("customer_rate");
                    BigDecimal bdValue = new BigDecimal(value);
                    results =  bdValue.setScale(0, RoundingMode.HALF_UP).intValue();
                }
                rs.close();
            }
        }
        
        return results;
    }
    
    /**
     * ÅÉãZèpãqíPâøñ⁄ïWê›íËÅÑ.ëÂÇ‹Ç©Ç»ãZèpãqíPâøñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢.ëOä˙é¿ê—
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param year Integer.
     * @param type Integer.
     * @return List<Integer>.
     * @throws SQLException 
     */
    public List<Integer> loadDataTrophyCustomer(ConnectionWrapper con, Integer shopId, Integer shopCategoryId, Integer year, int type, int periodMonth) throws SQLException
    {
        ArrayList<Integer> arrTrophyCustomer = new ArrayList<Integer>();
        ResultSetWrapper	rs	=	con.executeQuery( this.getDataTrophyCustomerSQL(shopId, shopCategoryId, year,  type, periodMonth));

        while(rs.next())
        {
            arrTrophyCustomer.add(rs.getInt("total_value"));
            arrTrophyCustomer.add(rs.getInt("total_num"));
        }
        rs.close();
        return arrTrophyCustomer;
    }
    /**
     * ÅÉãZèpãqíPâøñ⁄ïWê›íËÅÑ.ëÂÇ‹Ç©Ç»èóê´ãqêîî‰ó¶Çê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢.ñ⁄ïW
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param year Integer.
     * @return Double.
     * @throws SQLException 
     */
    public Double loadSumFemaleRate(ConnectionWrapper con,Integer shopId, Integer shopCategoryId, Integer year) throws SQLException
    {
        ResultSetWrapper	rs	=	con.executeQuery( this.getSumFemaleRateSQL(shopId, shopCategoryId, year));
        Double sum = 0d;
        if(rs.next())
        {
            sum = rs.getDouble("sum_female_rate");
        }
        rs.close();
        return sum;
    }
    /**
     * ÅÉãZèpãqíPâøñ⁄ïWê›íËÅÑ.ëÂÇ‹Ç©Ç»ãZèpãqíPâøñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢.èóê´íPâø
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param year Integer
     * @return Integer.
     * @throws SQLException 
     */
    public Integer loadSumFemaleUnitPrice(ConnectionWrapper con,Integer shopId, Integer shopCategoryId, Integer year) throws SQLException
    {
        ResultSetWrapper	rs	=	con.executeQuery( this.getSumFemaleUnitPriceSQL(shopId, shopCategoryId, year));
        Integer sum = 0;
        if(rs.next())
        {
            sum = rs.getInt("sum_female_unit_price");
        }
        rs.close();
        return sum;
    }
    /**
     * ÅÉãZèpãqíPâøñ⁄ïWê›íËÅÑ.ëÂÇ‹Ç©Ç»ãZèpãqíPâøñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢.íjê´íPâø
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param year Integer.
     * @return Integer.
     * @throws SQLException 
     */
    public Integer loadSumMaleUnitPrice(ConnectionWrapper con,Integer shopId, Integer shopCategoryId, Integer year) throws SQLException
    {
        ResultSetWrapper	rs	=	con.executeQuery( this.getSumMaleUnitPriceSQL(shopId, shopCategoryId, year));
        Integer sum = 0;
        if(rs.next())
        {
            sum = rs.getInt("sum_male_unit_price");
        }
        rs.close();
        return sum;
    }
    /**
     * ÅÉäeçƒóàó¶ÅAéñëOó\ñÒó¶ÅAéüâÒó\ñÒó¶ñ⁄ïWê›íËÅÑ.çƒóàó¶ñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢
   * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param date String
     * @param type Integer.
     * @param valueMonth Integer.
     * @return Integer.
     * @throws SQLException 
     */
    public Integer loadCountCustomer (ConnectionWrapper con,Integer shopId, Integer shopCategoryId, String date,int type, int valueMonth) throws SQLException
    {
        //IVS_LVTu start edit 2015/01/23 Task #35026
        StringBuilder sql = new StringBuilder(1000);
        //sql.append("SELECT count(customer_id) as count ");
        sql.append("SELECT count(view_data_sales_valid.customer_id) as count ");
        sql.append("    FROM view_data_sales_valid ");
        if(type == 0) {
            sql.append("    INNER JOIN data_reservation dr  ");
            sql.append("    ON view_data_sales_valid.customer_id = dr.customer_id ");
            sql.append("    AND view_data_sales_valid.shop_id = dr.shop_id ");
            sql.append("    AND view_data_sales_valid.slip_no = dr.slip_no ");
            sql.append("    AND dr.delete_date is null ");
        }
        sql.append("  WHERE   ");
        // IVS_nahoang start add ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶ 
        // Bug #31800 [Phase4][Product][Code][Edit]ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶ 20141027
        if (shopCategoryId != 0) {
            sql.append("    exists (");
            sql.append("          (   SELECT 1 ");
            sql.append("                            FROM");
            sql.append("                                data_sales_detail dsd");
            sql.append("                                inner join mst_technic mst on mst.technic_id = dsd.product_id and dsd.product_division = 1");
            sql.append("                                left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
            sql.append("                            WHERE");
            sql.append("                                dsd.shop_id = view_data_sales_valid.shop_id");
            sql.append("                                AND dsd.slip_no = view_data_sales_valid.slip_no");
            sql.append("                                AND dsd.delete_date is null");
            sql.append("                                AND dsd.product_division in (1)");
            sql.append("                                 AND mstc.shop_category_id = " + shopCategoryId + "");
            sql.append("                   )");
            sql.append("        union all");
            sql.append("                        (SELECT  1");
            sql.append("                            FROM");
            sql.append("                                data_sales_detail dsd");
            sql.append("                                inner join mst_item msi on msi.item_id = dsd.product_id and dsd.product_division = 2  ");
            sql.append("                                left join mst_item_class msic on msic.item_class_id = msi.item_class_id ");
            sql.append("                            WHERE");
            sql.append("                                dsd.shop_id = view_data_sales_valid.shop_id");
            sql.append("                                AND dsd.slip_no = view_data_sales_valid.slip_no");
            sql.append("                                AND dsd.delete_date is null");
            sql.append("                                AND dsd.product_division in (2)");
            sql.append("                                 AND msic.shop_category_id =" + shopCategoryId + "");
            sql.append(" )");
            sql.append("  ) AND ");
        }
        
        //IVS_nahoang end add ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶
        sql.append("  sales_date < date '" + date + "'-interval'" + valueMonth + " month' ");
        sql.append("          AND sales_date >= date '" + date + "' - interval '1 month' -interval'" + valueMonth + " month' ");
        if(type == 0)
        {
            sql.append("          AND visit_num = 1 ");
            sql.append("          AND dr.mobile_flag > 0 ");
        }
        if (type == 1) {
            sql.append("     AND visit_num = 1 ");
        }
        if (type == 2) {
            sql.append("      and visit_num in (2,3) ");
        }
        if (type == 3) {
            sql.append("     AND visit_num >= 4 ");
        }
        //sql.append("   AND shop_id = " + shopId + "");
        sql.append("   AND view_data_sales_valid.shop_id = " + shopId + "");
        ResultSetWrapper	rs1	=	con.executeQuery( sql.toString());
        //IVS_LVTu end edit 2015/01/23 Task #35026
        Integer sum = 0;
        if(rs1.next())
        {
            Integer count = rs1.getInt("count");
            if(count > 0)
            {
                ResultSetWrapper	rs	=	con.executeQuery( this.getSelectCountCustomerSQL(shopId, shopCategoryId, date,type, valueMonth));
                if( rs.next())
                {
                    //sum = rs.getInt("customer_return_rate");
                    Double value = 0d;
                    value = rs.getDouble("customer_return_rate");
                    BigDecimal bdValue = new BigDecimal(value);
                    sum =  bdValue.setScale(0, RoundingMode.HALF_UP).intValue();
                }
                rs.close();
            }
            
        }
        rs1.close();
        return sum;
    }
    /**
     * ÅÉäeçƒóàó¶ÅAéñëOó\ñÒó¶ÅAéüâÒó\ñÒó¶ñ⁄ïWê›íËÅÑ. 
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param year Integer.
     * @param type Integer.
     * @return ArrayList<Integer>
     * @throws SQLException 
     */
    public ArrayList<Integer> loadTargetRate (ConnectionWrapper con,Integer shopId, Integer shopCategoryId, Integer year,int type) throws SQLException
    {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        ResultSetWrapper	rs	=	con.executeQuery( this.getSelectTargetRateSQL(shopId, shopCategoryId, year,type));
        if( rs.next())
        {
            Double temp ;
            temp = rs.getDouble("target90");
            temp = temp*100 + 0.5;
            arr.add(temp.intValue());
            temp = rs.getDouble("target120");
            temp = temp*100 + 0.5;
            arr.add(temp.intValue());
            temp = rs.getDouble("target180");
            temp = temp*100 + 0.5;
            arr.add(temp.intValue());
        }
        else
        {
            arr.add(0);
            arr.add(0);
            arr.add(0);
        }
        rs.close();
        return arr;
    }
    /**
     * ÅÉäeçƒóàó¶ÅAéñëOó\ñÒó¶ÅAéüâÒó\ñÒó¶ñ⁄ïWê›íËÅÑ.éñëOó\ñÒó¶ÅAéüâÒó\ñÒó¶ñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢.ëOä˙
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param year Integer
     * @param date String
     * @param type Integer
     * @return Integer
     * @throws SQLException 
     */
    public Integer loadReservationRate (ConnectionWrapper con,Integer shopId,Integer shopCategoryId, Integer year,String date, int type, Integer periodMonth) throws SQLException
    {
        StringBuilder sql = new StringBuilder(1000);
        
        //IVS_nahoang edit 20141028 
        // [Phase4][Product][Code][Edit]ñ⁄ïWè⁄ç◊ê›íË_ éñëOó\ñÒó¶,éüâÒó\ñÒó¶	PHAN NAY CHUA TINH THEO NENDO( 4~3) 
        sql.append("     (select count(dr.reservation_no) as count from data_reservation dr  " );
        sql.append(" inner join data_reservation_detail drd on drd.shop_id = dr.shop_id and drd.reservation_no= dr.reservation_no " );
        if(shopCategoryId != 0)
        {
            sql.append(" and course_flg is null  " );
            sql.append(" inner join mst_technic mt on mt.technic_id = drd.technic_id " );
            sql.append(" inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id " );
        }
        //sql.append("    inner join data_sales ds on ds.shop_id = ds.shop_id and ds.slip_no = dr.slip_no and ds.customer_id = dr.customer_id    " );
        sql.append("  where  " );
        sql.append("  dr.delete_date is null and drd.delete_date is null " );
        sql.append("  and dr.shop_id = "+shopId+"  " );
        if(shopCategoryId != 0)
        {
            sql.append("    and mtc.shop_category_id = "+shopCategoryId+" ");
            sql.append("    and drd.delete_date is null ");
        }
        
        if(periodMonth == 12) {
           sql.append("  and date_part('year',drd.reservation_datetime) = date_part('year',date '"+date+"' - interval '1 year' )" ); 
        }else{
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            sql.append("  and ((date_part('year',drd.reservation_datetime) = date_part('year',date '"+date+"' - interval '1 year' ) and date_part('month',drd.reservation_datetime) >" + periodMonth +") " ); 
            sql.append("  or (date_part('year',drd.reservation_datetime) = date_part('year',date '"+date+"' ) and date_part('month',drd.reservation_datetime) <= " +  periodMonth + "))" ); 
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        }
        sql.append(" )");
        
        //IVS_nahoang end edit
        ResultSetWrapper	rs1	=	con.executeQuery( sql.toString());
        Integer reservationRate = 0;
        if(rs1.next())
        {
            Integer count = rs1.getInt("count");
            if(count > 0)
            {
                ResultSetWrapper	rs	=	con.executeQuery( this.getSelectReservationRateSQL(shopId, shopCategoryId, year, date, type, periodMonth));
                if( rs.next())
                {
                    //reservationRate = rs.getInt("Reservation_rate");
                    Double value = 0d;
                    value = rs.getDouble("Reservation_rate");
                    BigDecimal bdValue = new BigDecimal(value);
                    reservationRate =  bdValue.setScale(0, RoundingMode.HALF_UP).intValue();

                }
                rs.close();
            }
        }
        rs1.close();
        return reservationRate;
    }
    /**
     * ÅÉäeçƒóàó¶ÅAéñëOó\ñÒó¶ÅAéüâÒó\ñÒó¶ñ⁄ïWê›íËÅÑ.éñëOó\ñÒó¶ÅAéüâÒó\ñÒó¶ñ⁄ïWÇê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢.ñ⁄ïW
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param year Integer.
     * @return DataTarget.
     * @throws SQLException 
     */
    public DataTarget loadTargetReservationRate (ConnectionWrapper con,Integer shopId,Integer shopCategoryId, Integer year) throws SQLException
    {
        DataTarget dt = new DataTarget();
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select before_reserve, next_reserve ");
        sql.append(" from data_target ");
        sql.append(" where shop_id ="+shopId+" ");
        sql.append(" and shop_category_id ="+shopCategoryId+" ");
        sql.append(" and month = 12 ");
        sql.append(" and year ="+year+" ");
        
        ResultSetWrapper	rs	=	con.executeQuery( sql.toString());
        if( rs.next())
        {
            Double beforeReserve = rs.getDouble("before_reserve");
            Double nextReserve = rs.getDouble("next_reserve");
            dt.setBeforeReserve(beforeReserve*100);
            dt.setNextReserve(nextReserve*100);
        }
        rs.close();
        return dt;
    }
    /**
     * ÅÉêVãKî‰ó¶ñ⁄ïWê›íËÅÑ.êVãKî‰ó¶.ëOä˙
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param year Integer
     * @param month Integer
     * @return Integer
     * @throws SQLException 
     */
    public Integer loadPreviousPeriodRate (ConnectionWrapper con,Integer shopId, Integer year, Integer periodMonth, Integer shopCategoryId) throws SQLException
    {
        Integer previousPeriod = 0;
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select count(DISTINCT ds.slip_no)  as count from view_data_sales_detail_valid ds " );
        sql.append("   where ds.shop_id ="+shopId+" " );
        if (periodMonth == 12) {
            sql.append("   and ((date_part('year',ds.sales_date) = " + (year -1) + " ");
            sql.append(" ) )");
        } else {
            sql.append("   and ((date_part('year',ds.sales_date) = " + (year -1) + " ");
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            sql.append("   and date_part('month', ds.sales_date) > " + periodMonth + ") ");
            sql.append(" or (date_part('year',ds.sales_date) =" + year + " ");
            sql.append(" AND date_part('month', ds.sales_date) <= " + periodMonth + ")) ");
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        }
        // IVS_nahoang start add ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶  20141027
        if(shopCategoryId != 0){
            sql.append(" AND exists ((SELECT 1");
            sql.append("            FROM");
            sql.append("            data_sales_detail dsd");
            sql.append("            inner join mst_technic mst on mst.technic_id = dsd.product_id and dsd.product_division = 1");
            sql.append("            left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
            sql.append("        WHERE");
            sql.append("            dsd.shop_id = ds.shop_id");
            sql.append("            AND dsd.slip_no = ds.slip_no");
            sql.append("            AND dsd.delete_date is null");
            sql.append("            AND dsd.product_division in (1)");
            sql.append("             AND mstc.shop_category_id = "+ shopCategoryId +"");
            sql.append("  )");
            sql.append("             union all");
            sql.append("             (SELECT 1 ");
            sql.append("        FROM");
            sql.append("            data_sales_detail dsd");
            sql.append("            inner join mst_item msi on msi.item_id = dsd.product_id and dsd.product_division = 2");
            sql.append("            left join mst_item_class msic on msic.item_class_id = msi.item_class_id ");
            sql.append("        WHERE");
            sql.append("            dsd.shop_id = ds.shop_id");
            sql.append("            AND dsd.slip_no = ds.slip_no");
            sql.append("            AND dsd.delete_date is null");
            sql.append("            AND dsd.product_division in (2)");
            sql.append("             AND msic.shop_category_id = "+ shopCategoryId +"");  
            sql.append("      )  )");
        }
            
        // IVS_nahoang end add ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶         
        ResultSetWrapper	rs1	=	con.executeQuery( sql.toString());
        
        if(rs1.next())
        {
            Integer valueCount = rs1.getInt("count");
            if(valueCount > 0)
            {
                ResultSetWrapper	rs	=	con.executeQuery( this.getSelectPreviousPeriodSQL(shopId, year,periodMonth, shopCategoryId));
                if( rs.next())
                {
                    //previousPeriod = rs.getInt("previous_period");
                    Double value = 0d;
                    value = rs.getDouble("previous_period");
                    BigDecimal bdValue = new BigDecimal(value);
                    previousPeriod =  bdValue.setScale(0, RoundingMode.HALF_UP).intValue();

                }
                rs.close();
            }
        }
        rs1.close();
        return previousPeriod;
    }
    /**
     * ÅÉêVãKî‰ó¶ñ⁄ïWê›íËÅÑ.êVãKî‰ó¶.ñ⁄ïW
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param year Integer
     * @return Integer
     * @throws SQLException 
     */
    public Integer loadTargetNewRate (ConnectionWrapper con,Integer shopId, Integer shopCategoryId, Integer year) throws SQLException
    {
        Integer targetNewRate = 0;
        ResultSetWrapper	rs	=	con.executeQuery( this.getSelectTargetNewRateSQL(shopId, shopCategoryId, year));
        if( rs.next())
        {
            Double temp ;
            temp = rs.getDouble("new_rate");
            temp = temp*100;
            targetNewRate = temp.intValue();
        }
        else
        {
            targetNewRate = 0;
        }
        rs.close();
        return targetNewRate;
    }
    /**
     * ÅÉî}ëÃï êVãKñ⁄ïWê›íËÅÑ.
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param date String
     * @param firstComingMotiveClassId Integer
     * @throws SQLException 
     */
    public void loadDataTargetMotive (ConnectionWrapper con,Integer shopId, Integer shopCategoryId, String date, Integer firstComingMotiveClassId) throws SQLException
    {
        ResultSetWrapper	rs	=	con.executeQuery( this.getSelectDataTargetMotiveSQL(shopId, shopCategoryId, date, firstComingMotiveClassId));
        while(rs.next())
        {
            DataTargetMotive dataTargetMotive = new DataTargetMotive();
            dataTargetMotive.setYear(rs.getInt("year"));
            dataTargetMotive.setMonth(rs.getInt("month")) ;
            dataTargetMotive.setNum(rs.getInt("num_value"));
            //IVS_LVTu start add 2015/01/22 Task #35026
            dataTargetMotive.setOwn_flg(rs.getInt("own_flg"));
            //IVS_LVTu end add 2015/01/22 Task #35026
            this.arrDataTargetMotive.add(dataTargetMotive);
        }
        rs.close();
    }
    
    /**
     * ÅÉÉfÉVÉãñ⁄ïWê›íËÅÑ.ñ⁄ïWî‰ó¶ and ñ⁄ïWóàìXâÒêî
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param year Integer
     * @return DataTarget
     * @throws SQLException 
     */
    public DataTarget loadDecylTarget (ConnectionWrapper con,Integer shopId,Integer shopCategoryId, Integer year) throws SQLException
    {
        DataTarget dt =  new DataTarget();
        StringBuilder sql = new StringBuilder(1000);
        sql.append("	select decyl_1_rate, decyl_1_num, decyl_2_rate, decyl_2_num, decyl_3_rate, decyl_3_num ");
        sql.append("	from data_target ");
        sql.append("	where delete_date is null ");
        sql.append("	and shop_id ="+shopId+" ");
        sql.append("	and shop_category_id ="+shopCategoryId+" ");
        sql.append("	and year ="+year+" ");
        sql.append("	and month = 12 ");
        
        ResultSetWrapper	rs	=       con.executeQuery(sql.toString());
        if(rs.next())
        {
            dt.setDecyl_1Rate(rs.getInt("decyl_1_rate"));
            dt.setDecyl_1Num(rs.getInt("decyl_1_num"));
            dt.setDecyl_2Rate(rs.getInt("decyl_2_rate"));
            dt.setDecyl_2Num(rs.getInt("decyl_2_num"));
            dt.setDecyl_3Rate(rs.getInt("decyl_3_rate"));
            dt.setDecyl_3Num(rs.getInt("decyl_3_num"));
        }
        rs.close();
        return dt;
    }
    /**
     * ÅÉäeÉÅÉjÉÖÅ[î‰ó¶ñ⁄ïWê›íËÅÑ
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param year Integer
     * @param month Integer
     * @throws SQLException 
     */
    public void loadSetTargetRateMenu (ConnectionWrapper con,Integer shopId,Integer shopCategoryId, Integer year, Integer periodMonth) throws SQLException
    {
        ResultSetWrapper	rs	=	con.executeQuery( this.getSelectSetTargetRateMenuSQL(shopId,shopCategoryId,  year, periodMonth));
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" 	select technic_class_id, technic_class_name, ");
        sql.append(" 	coalesce((select rate*100 from data_target_technic ");
        sql.append(" where year= " + year + " and technic_class_id = mtc.technic_class_id ");
        sql.append(" and shop_category_id = "+shopCategoryId+"");
        sql.append(" and shop_id = "+shopId+"");
        sql.append(" and month = 12),0) ::bigint as technic_target_rate, ");
        sql.append(" 	coalesce((select value from data_target_technic ");
        sql.append(" where year= " + year + " and technic_class_id = mtc.technic_class_id ");
        sql.append(" and shop_category_id = "+shopCategoryId+"");
        sql.append(" and shop_id = "+shopId+"");
        sql.append(" and month = 12),0) :: bigint as technic_target_price ");
        sql.append(" 	from mst_technic_class mtc ");
        sql.append(" 	where mtc.delete_date is null ");
        //IVS_LVTu start 2015/01/29 
        sql.append("   AND mtc.prepaid = 0 " );
        //IVS_LVTu end 2015/01/29 
        sql.append(" 	AND EXISTS ");
        sql.append(" 	(SELECT 1 ");
        sql.append(" 	FROM mst_use_product mup ");
        sql.append(" 	INNER JOIN mst_technic mt ON mup.product_id = mt.technic_id ");
        sql.append(" 	WHERE mup.shop_id = " + shopId + " ");
        sql.append(" 	AND mup.product_division = 1 ");
        sql.append(" 	AND mt.technic_class_id = mtc.technic_class_id) ");
        //IVS_LVTu start add 2014/12/02 Mashu_ñ⁄ïWä«óùâÊñ  Task #33427
        if(shopCategoryId != 0)
        {
            sql.append(" 	 and mtc.technic_class_id in (SELECT technic_class_id FROM mst_technic_class " );
            sql.append(" 	   WHERE shop_category_id = "+ shopCategoryId +" " );
            sql.append(" 	   and delete_date is null) " );
        }
        //IVS_LVTu end add 2014/12/02 Mashu_ñ⁄ïWä«óùâÊñ  Task #33427
        sql.append(" 	ORDER BY mtc.display_seq ");
        
        
        ResultSetWrapper	rs1	=	con.executeQuery( sql.toString());
        rs.beforeFirst();
        while(rs.next() && rs1.next())
        {
            SetTargetMenu setTargetMenu = new SetTargetMenu();
            setTargetMenu.technicClassId = (rs.getInt("technic_class_id"));
            setTargetMenu.technicClassName = (rs.getString("technic_class_name"));
            setTargetMenu.salesValue = (rs.getInt("sales_value"));
            setTargetMenu.customerCount = (rs.getInt("customer_count"));
            setTargetMenu.totalNum = (rs.getInt("total_num"));
            
            setTargetMenu.technicTargetRate = (rs1.getInt("technic_target_rate"));
            setTargetMenu.technicTargetPrice = (rs1.getInt("technic_target_price"));
            
            this.setTargetMenu.add(setTargetMenu);
        }
        rs1.close();
        rs.close();
    }
    /**
     * ÅÉè§ïiï î‰ó¶ñ⁄ïWê›íËÅÑ. ï™óﬁï Ç…ñ⁄ïWî‰ó¶Çê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param year Integer
     * @param month Integer
     * @throws SQLException 
     */
    public void loadProductTarget (ConnectionWrapper con,Integer shopId,Integer shopCategoryId, Integer year, Integer periodMonth) throws SQLException
    {
        ResultSetWrapper	rs	=	con.executeQuery( this.getSelectSetProductTargetSQL(shopId,shopCategoryId,  year, periodMonth));
        while(rs.next())
        {
            SetProductTarget setProductTarget = new SetProductTarget();
            setProductTarget.itemClassId = (rs.getInt("item_class_id"));
            setProductTarget.item_class_name = (rs.getString("item_class_name"));
            setProductTarget.salesValue = (rs.getInt("sales_value"));
            
            this.setProductTarget.add(setProductTarget);
        }
        rs.close();
    }
    /**
     * ÅÉè§ïiï î‰ó¶ñ⁄ïWê›íËÅÑ. ï™óﬁï Ç…ñ⁄ïWî‰ó¶Çê›íËÇµÇƒÇ≠ÇæÇ≥Ç¢.ñ⁄ïWî‰ó¶
     * @param con ConnectionWrapper
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param year Integer
     * @throws SQLException 
     */
    public void loadSetProductTarget (ConnectionWrapper con,Integer shopId,Integer shopCategoryId, Integer year) throws SQLException
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" 	select item_class_id, item_class_name, " );
        sql.append(" 		coalesce((select rate*100 from data_target_item   " );
        sql.append(" 		where year="+year+" and month = 12  " );
        sql.append(" 		and shop_id = "+shopId+" " );
        sql.append(" 		and shop_category_id = "+shopCategoryId+" " );
        sql.append(" 		and item_class_id = mic.item_class_id ),0) ::bigint as item_target_rate " );
        sql.append(" 	from mst_item_class mic " );
        sql.append(" 	where mic.delete_date is null " );
        sql.append(" 	and exists " );
        sql.append(" 	(select 1 " );
        sql.append(" 	 FROM mst_use_product mup " );
        sql.append(" 	 inner join mst_item mi ON mup.product_id = mi.item_id " );
        //sql.append(" 	 WHERE mup.shop_id IN (1) " );
        sql.append(" 	 WHERE mup.shop_id = "+shopId+" " );
        sql.append(" 	   AND mup.product_division = 2 " );
        sql.append(" 	   AND mi.item_class_id = mic.item_class_id) " );
        //IVS_LVTu start add 2014/12/02 Mashu_ñ⁄ïWä«óùâÊñ  Task #33427
        if(shopCategoryId != 0)
        {
            sql.append(" 	 and mic.item_class_id in (SELECT item_class_id FROM mst_item_class " );
            sql.append(" 	   WHERE shop_category_id = "+ shopCategoryId +" " );
            sql.append(" 	   and delete_date is null) " );
        }
        //IVS_LVTu end add 2014/12/02 Mashu_ñ⁄ïWä«óùâÊñ  Task #33427
        sql.append(" ORDER BY  mic.display_seq " );

        ResultSetWrapper	rs	=	con.executeQuery( sql.toString());
        while(rs.next())
        {
            SetProductTarget setProductTarget = new SetProductTarget();
            setProductTarget.itemTargetRate = (rs.getInt("item_target_rate"));
            
            this.setProductTarget.add(setProductTarget);
        }
        rs.close();
    }
    /**
     * ÅÉãZèpîÑè„ñ⁄ïWê›íËÅÑ.åéäiç∑ÇéQçlÇ…ÇµÇƒñàåéÇäÑÇËêUÇÈÇ∆â∫ãLÇÃÇÊÇ§Ç…Ç»ÇËÇ‹Ç∑ÅB
     * @param shopId
     * @param shopCategoryId
     * @param date
     * @return SQL.
     */
    public String getSelectByShopIdByShopCategoryIdSQL(Integer shopId, Integer shopCategoryId, String date, int type)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select  " );
        sql.append(" b.year,b.month,  " );
        sql.append(" (  " );
        if(type == 1)
        {
            sql.append(" select coalesce(sum(technic),0)   " );
        }
        else
        {
            sql.append(" select coalesce(sum(item),0)   " );
        }
        sql.append(" from data_target dt  " );
        sql.append(" where dt.shop_id = "+ shopId +"    " );
        sql.append(" and dt.shop_category_id = " + shopCategoryId + "  " );
        sql.append(" and dt.year = b.year and dt.month = b.month  " );
        sql.append(" ) as technic_value  " );
        sql.append(" from   " );
        sql.append(" (  " );
        sql.append(" 	select date_part('year',a.date) as year,date_part('month',a.date) as month  " );
        sql.append(" 	from  " );
        sql.append(" 	(  " );
        sql.append(" 	select date'"+ date + "' +interval'0 month'as date  " );
        sql.append(" 	union   " );
        sql.append(" 	select date '"+ date + "'+interval'1 month' as date  " );
        sql.append(" 	union   " );
        sql.append(" 	select date '"+ date + "'+interval'2 month' as date  " );
        sql.append(" 	union   " );
        sql.append(" 	select date '"+ date + "'+interval'3 month' as date  " );
        sql.append(" 	union  " );
        sql.append(" 	select date '"+ date + "'+interval'4 month' as date  " );
        sql.append(" 	union  " );
        sql.append(" 	select date '"+ date + "'+interval'5 month' as date  " );
        sql.append(" 	union  " );
        sql.append(" 	select date '"+ date + "'+interval'6 month' as date  " );
        sql.append(" 	union  " );
        sql.append(" 	select date '"+ date + "'+interval'7 month' as date  " );
        sql.append(" 	union  " );
        sql.append(" 	select date '"+ date + "'+interval'8 month' as date  " );
        sql.append(" 	union  " );
        sql.append(" 	select date '"+ date + "'+interval'9 month' as date  " );
        sql.append(" 	union  " );
        sql.append(" 	select date '"+ date + "'+interval'10 month' as date  " );
        sql.append(" 	union  " );
        sql.append(" 	select date '"+ date + "'+interval'11 month' as date  " );
        sql.append("   " );
        sql.append(" 	)a  " );
        sql.append(" )b  " );
        sql.append(" order by b.year,b.month ");
        
        return sql.toString();
    }
    /**
     * ÅÉãZèpîÑè„ñ⁄ïWê›íËÅÑ. Row ãZîÑåéñ⁄ïW
     * @param shopId Integer
     * @param shopCategoryId Integer
     * @param date String
     * @param type 1: product_division= 1 2: and product_division= 2
     * @return sql String.
     */
    public String getDataSalesByShopIdSQL(Integer shopId, Integer shopCategoryId, String date, int type)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select  " );
        sql.append(" b.year,b.month, " );
        sql.append(" ( " );
        sql.append(" select coalesce(sum(discount_detail_value_no_tax),0)  " );
        sql.append(" from view_data_sales_detail_valid vdsd " );
        if(type == 1)
        {
            sql.append(" 	 inner join mst_technic mt on vdsd.product_id = mt.technic_id  " );
            sql.append(" 	 inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id " );
            sql.append(" where shop_id = "+ shopId +" and product_division= 1  " );
        }
        else
        {
            sql.append(" 	 inner join mst_item mi on vdsd.product_id = mi.item_id  " );
            sql.append(" 	 inner join mst_item_class mic on mic.item_class_id = mi.item_class_id " );
            sql.append(" where shop_id = "+ shopId +" and product_division= 2  " );
        }
        if(shopCategoryId != 0)
        {
            if(type == 1)
            {
                sql.append(" and mtc.shop_category_id = " + shopCategoryId + "  " );
            }
            else
            {
                sql.append(" and mic.shop_category_id = " + shopCategoryId + "  " );
            }
        }
        sql.append(" and date_part('year',sales_date) = b.year and date_part('month',sales_date) = b.month " );
        sql.append(" ) as technic_value " );
        sql.append(" from  " );
        sql.append(" ( " );
        sql.append(" 	select date_part('year',a.date) as year,date_part('month',a.date) as month " );
        sql.append(" 	from " );
        sql.append(" 	( " );
        sql.append(" 	select date'"+ date + "' -interval'1 month'as date " );
        sql.append(" 	union " );
        sql.append(" 	select date '"+ date + "'-interval'2 month' as date " );
        sql.append(" 	union " );
        sql.append(" 	select date '"+ date + "'-interval'3 month' as date " );
        sql.append(" 	union " );
        sql.append(" 	select date '"+ date + "'-interval'4 month' as date " );
        sql.append(" 	union " );
        sql.append(" 	select date '"+ date + "'-interval'5 month' as date " );
        sql.append(" 	union " );
        sql.append(" 	select date '"+ date + "'-interval'6 month' as date " );
        sql.append(" 	union " );
        sql.append(" 	select date '"+ date + "'-interval'7 month' as date " );
        sql.append(" 	union " );
        sql.append(" 	select date '"+ date + "'-interval'8 month' as date " );
        sql.append(" 	union " );
        sql.append(" 	select date '"+ date + "'-interval'9 month' as date " );
        sql.append(" 	union " );
        sql.append(" 	select date '"+ date + "'-interval'10 month' as date " );
        sql.append(" 	union " );
        sql.append(" 	select date '"+ date + "'-interval'11 month' as date " );
        sql.append(" 	union " );
        sql.append(" 	select date '"+ date + "'-interval'12 month' as date " );
        sql.append("  " );
        sql.append(" 	)a " );
        sql.append(" )b " );
        sql.append(" order by b.year, b.month " );
        return sql.toString();
    }
    
    
    public String getDataCustomerSQL(Integer shopId, Integer shopCategoryId, Integer year, int periodMonth )
    {
        StringBuilder sql = new StringBuilder(1000);
        //IVS_LVTu start edit 2014/10/29 Mashu_ê›íËâÊñ 
        sql.append("  SELECT " );
        sql.append("  (SELECT " );
        sql.append("	 (SELECT coalesce(count(DISTINCT vdsd.slip_no),0) " );
        sql.append("	  FROM view_data_sales_detail_valid vdsd " );
        sql.append("	  INNER JOIN mst_technic mt ON vdsd.product_id = mt.technic_id " );
        sql.append("	  INNER JOIN mst_technic_class mtc ON mtc.technic_class_id = mt.technic_class_id " );
        sql.append("	  INNER JOIN mst_customer mc ON mc.customer_id = vdsd.customer_id AND mc.customer_no <> '0' " );
        sql.append("	  WHERE vdsd.shop_id ="+shopId+" " );
        sql.append("		AND product_division = 1 " );
        if(shopCategoryId != 0)
        {
            sql.append("		AND mtc.shop_category_id = "+shopCategoryId+" " );
        }
        // type = 1 when periodMonth = 12 , type = 2 when periodMonth = 3
        //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        if(periodMonth == 12)
        {
            sql.append("		AND date_part('year',sales_date) = "+year+"-1  " );
        }
        else
        {
            //sql.append("		AND ((date_part('year',sales_date) = "+year+"-1  " );
            //sql.append("		AND date_part('month',sales_date) > 3) or (date_part('year',sales_date) = "+year+" " );
            //sql.append("		AND date_part('month',sales_date) < 4)) " );
            sql.append("		AND ((date_part('year',sales_date) = "+year+"-1  " );
            sql.append("		AND date_part('month',sales_date) >"+ periodMonth +") or (date_part('year',sales_date) = "+year+" " );
            sql.append("		AND date_part('month',sales_date) <=" + periodMonth +")) " );
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        }
        sql.append("		AND mc.sex = 2 " );
        sql.append("		))*100.0 / " );
        sql.append("  (SELECT " );
        sql.append("	 (SELECT coalesce(count(DISTINCT vdsd.slip_no),0) " );
        sql.append("	  FROM view_data_sales_detail_valid vdsd " );
        sql.append("	  INNER JOIN mst_technic mt ON vdsd.product_id = mt.technic_id " );
        sql.append("	  INNER JOIN mst_technic_class mtc ON mtc.technic_class_id = mt.technic_class_id " );
        sql.append("	  INNER JOIN mst_customer mc ON mc.customer_id = vdsd.customer_id AND mc.customer_no <> '0' " );
        sql.append("	  WHERE vdsd.shop_id ="+shopId+" " );
        sql.append("		AND product_division = 1 " );
        if(shopCategoryId != 0)
        {
            sql.append("		AND mtc.shop_category_id = "+shopCategoryId+" " );
        }
        // type = 1 when periodMonth = 12 , type = 2 when periodMonth = 3
        //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        if(periodMonth == 12)
        {
            sql.append("		AND date_part('year',sales_date) = "+year+"-1  " );
        }
        else
        {
            
            //sql.append("		AND ((date_part('year',sales_date) = "+year+"-1  " );
            //sql.append("		AND date_part('month',sales_date) > 3) or (date_part('year',sales_date) = "+year+" " );
            //sql.append("		AND date_part('month',sales_date) < 4)) " );
            sql.append("		AND ((date_part('year',sales_date) = "+year+"-1  " );
            sql.append("		AND date_part('month',sales_date) >"+ periodMonth +") or (date_part('year',sales_date) = "+year+" " );
            sql.append("		AND date_part('month',sales_date) <=" + periodMonth +")) " );
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        }
        sql.append("		)) AS customer_rate ");
        //IVS_LVTu end edit 2014/10/29 Mashu_ê›íËâÊñ 
        return sql.toString();
    }
    
    public String getDataTrophyCustomerSQL(Integer shopId, Integer shopCategoryId, Integer year, int type, int periodMonth)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append("	select coalesce(sum(discount_detail_value_no_tax),0) as total_value, ");
        sql.append("	count( distinct vdsd.slip_no) as total_num ");
        sql.append("	from view_data_sales_detail_valid vdsd ");
        sql.append("		 inner join mst_technic mt on vdsd.product_id = mt.technic_id and product_division = 1 ");
        sql.append("		 inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id ");
        sql.append("		 inner join mst_customer mc on mc.customer_id = vdsd.customer_id AND mc.customer_no <> '0' ");
        if(type == 1)
        {
            sql.append("		 and mc.sex =1 ");
        }
        else
        {
            sql.append("		 and mc.sex =2 ");
        }
        sql.append("	where vdsd.shop_id ="+shopId+" and product_division = 1 ");
        if(shopCategoryId != 0)
        {
            sql.append("	and mtc.shop_category_id = "+shopCategoryId+" ");
        }
        //sql.append("	and ((date_part('year',sales_date) = "+year+"-1 and date_part('month',sales_date) > 3) ");
        //sql.append("	or (date_part('year',sales_date) = 2014 and date_part('month',sales_date) < 4)) ");
        //sql.append("	or (date_part('year',sales_date) = "+year+" and date_part('month',sales_date) < 4)) ");
        
        if(periodMonth == 12)
        {
            sql.append("		AND date_part('year',sales_date) = "+year+"-1  " );
        }
        else
        {
            sql.append("		AND ((date_part('year',sales_date) = "+year+"-1  " );
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            sql.append("		AND date_part('month',sales_date) >"+ periodMonth +") or (date_part('year',sales_date) = "+year+" " );
            sql.append("		AND date_part('month',sales_date) <=" + periodMonth +")) " );
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        }
        return sql.toString();
    }
    
    public String getSumFemaleRateSQL(Integer shopId, Integer shopCategoryId, Integer year)
    {
        StringBuilder sql = new StringBuilder(1000);
        //sql.append( " select coalesce(sum(female_rate)/12,0) as sum_female_rate from data_target	" );
        sql.append( " select coalesce(female_rate,0) as sum_female_rate from data_target	" );
        sql.append( " where shop_id = " + shopId + "" );
        sql.append( " and shop_category_id = " +shopCategoryId+ "" );
        sql.append( " and year = " +year+ "" );
        sql.append( " and month = 12" );
        
        return sql.toString();
    }
    
    public String getSumFemaleUnitPriceSQL(Integer shopId,Integer shopCategoryId, Integer year)
    {
        StringBuilder sql = new StringBuilder(1000);
        //sql.append( " select coalesce(sum(female_unit_price)/12,0) as sum_female_unit_price from data_target	" );
        sql.append( " select coalesce(female_unit_price,0) as sum_female_unit_price from data_target	" );
        sql.append( " where shop_id = " + shopId + "" );
        sql.append( " and shop_category_id = " +shopCategoryId+ "" );
        sql.append( " and year = " +year+ "" );
        sql.append( " and month = 12" );
        
        return sql.toString();
    }
    
    public String getSumMaleUnitPriceSQL(Integer shopId,Integer shopCategoryId, Integer year)
    {
        StringBuilder sql = new StringBuilder(1000);
        //sql.append( " select coalesce(sum(male_unit_price)/12,0) as sum_male_unit_price from data_target	" );
        sql.append( " select coalesce(male_unit_price,0) as sum_male_unit_price from data_target	" );
        sql.append( " where shop_id = " + shopId + "" );
        sql.append( " and shop_category_id = " +shopCategoryId+ "" );
        sql.append( " and year = " +year+ "" );
        sql.append( " and month = 12" );
        
        return sql.toString();
    }
    
    public String getSelectCountCustomerSQL(Integer shopId, Integer shopCategoryId, String date,int type, int valueMonth)
    {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append("   (SELECT count(customer_id) ");
        sql.append("    FROM view_data_sales_valid ");
        sql.append("  WHERE   ");
        // IVS_nahoang start add ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶ 
        // Bug #31800 [Phase4][Product][Code][Edit]ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶ 20141027
        if (shopCategoryId != 0) {
            sql.append("    exists (");
            sql.append("          (   SELECT 1 ");
            sql.append("                            FROM");
            sql.append("                                data_sales_detail dsd");
            sql.append("                                inner join mst_technic mst on mst.technic_id = dsd.product_id and dsd.product_division = 1");
            sql.append("                                left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
            sql.append("                            WHERE");
            sql.append("                                dsd.shop_id = view_data_sales_valid.shop_id");
            sql.append("                                AND dsd.slip_no = view_data_sales_valid.slip_no");
            sql.append("                                AND dsd.delete_date is null");
            sql.append("                                AND dsd.product_division in (1)");
            sql.append("                                 AND mstc.shop_category_id = " + shopCategoryId + "");
            sql.append("                   )");
            sql.append("        union all");
            sql.append("                        (SELECT  1");
            sql.append("                            FROM");
            sql.append("                                data_sales_detail dsd");
            sql.append("                                inner join mst_item msi on msi.item_id = dsd.product_id and dsd.product_division = 2  ");
            sql.append("                                left join mst_item_class msic on msic.item_class_id = msi.item_class_id ");
            sql.append("                            WHERE");
            sql.append("                                dsd.shop_id = view_data_sales_valid.shop_id");
            sql.append("                                AND dsd.slip_no = view_data_sales_valid.slip_no");
            sql.append("                                AND dsd.delete_date is null");
            sql.append("                                AND dsd.product_division in (2)");
            sql.append("                                 AND msic.shop_category_id =" + shopCategoryId + "");
            sql.append(" )");
            sql.append("  )   AND ");
        }
        sql.append("    date_part('month',sales_date) = date_part('month',date '" + date + "' - interval '1 month') ");
        sql.append("   AND date_part('year',sales_date) = date_part('year',date '" + date + "' - interval '1 month') ");
        sql.append("   AND shop_id = " + shopId + " ");
        sql.append("   AND customer_id NOT IN ");
        sql.append("     (SELECT customer_id ");
        sql.append("   FROM view_data_sales_valid ");
        sql.append("  WHERE   ");
        if (shopCategoryId != 0) {
            sql.append("    exists (");
            sql.append("          (   SELECT 1 ");
            sql.append("                            FROM");
            sql.append("                                data_sales_detail dsd");
            sql.append("                                inner join mst_technic mst on mst.technic_id = dsd.product_id and dsd.product_division = 1");
            sql.append("                                left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
            sql.append("                            WHERE");
            sql.append("                                dsd.shop_id = view_data_sales_valid.shop_id");
            sql.append("                                AND dsd.slip_no = view_data_sales_valid.slip_no");
            sql.append("                                AND dsd.delete_date is null");
            sql.append("                                AND dsd.product_division in (1)");
            sql.append("                                 AND mstc.shop_category_id = " + shopCategoryId + "");
            sql.append("                   )");
            sql.append("        union all");
            sql.append("                        (SELECT  1");
            sql.append("                            FROM");
            sql.append("                                data_sales_detail dsd");
            sql.append("                                inner join mst_item msi on msi.item_id = dsd.product_id and dsd.product_division = 2  ");
            sql.append("                                left join mst_item_class msic on msic.item_class_id = msi.item_class_id ");
            sql.append("                            WHERE");
            sql.append("                                dsd.shop_id = view_data_sales_valid.shop_id");
            sql.append("                                AND dsd.slip_no = view_data_sales_valid.slip_no");
            sql.append("                                AND dsd.delete_date is null");
            sql.append("                                AND dsd.product_division in (2)");
            sql.append("                                 AND msic.shop_category_id =" + shopCategoryId + "");
            sql.append(" )");
            sql.append("  ) AND ");
        }
        sql.append("    sales_date >= date '" + date + "'-interval'" + valueMonth + " month' ");
        sql.append("     AND sales_date < date '" + date + "' - interval '1 month' ");
        sql.append("     AND shop_id = " + shopId + ") ");
        sql.append("   AND customer_id IN ");
        //IVS_LVTu start edit 2015/01/23 Task #35026
        //sql.append("     (SELECT customer_id ");
        sql.append("     (SELECT view_data_sales_valid.customer_id ");
        sql.append("   FROM view_data_sales_valid "); 
        if(type == 0) {
            sql.append("    INNER JOIN data_reservation dr  ");
            sql.append("    ON view_data_sales_valid.customer_id = dr.customer_id ");
            sql.append("    AND view_data_sales_valid.shop_id = dr.shop_id ");
            sql.append("    AND view_data_sales_valid.slip_no = dr.slip_no ");
            sql.append("    AND dr.delete_date is null ");
        }
        sql.append("  WHERE   ");
        if (shopCategoryId != 0) {
            sql.append("    exists (");
            sql.append("          (   SELECT 1 ");
            sql.append("                            FROM");
            sql.append("                                data_sales_detail dsd");
            sql.append("                                inner join mst_technic mst on mst.technic_id = dsd.product_id and dsd.product_division = 1");
            sql.append("                                left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
            sql.append("                            WHERE");
            sql.append("                                dsd.shop_id = view_data_sales_valid.shop_id");
            sql.append("                                AND dsd.slip_no = view_data_sales_valid.slip_no");
            sql.append("                                AND dsd.delete_date is null");
            sql.append("                                AND dsd.product_division in (1)");
            sql.append("                                 AND mstc.shop_category_id = " + shopCategoryId + "");
            sql.append("                   )");
            sql.append("        union all");
            sql.append("                        (SELECT  1");
            sql.append("                            FROM");
            sql.append("                                data_sales_detail dsd");
            sql.append("                                inner join mst_item msi on msi.item_id = dsd.product_id and dsd.product_division = 2  ");
            sql.append("                                left join mst_item_class msic on msic.item_class_id = msi.item_class_id ");
            sql.append("                            WHERE");
            sql.append("                                dsd.shop_id = view_data_sales_valid.shop_id");
            sql.append("                                AND dsd.slip_no = view_data_sales_valid.slip_no");
            sql.append("                                AND dsd.delete_date is null");
            sql.append("                                AND dsd.product_division in (2)");
            sql.append("                                 AND msic.shop_category_id =" + shopCategoryId + "");
            sql.append(" )");
            sql.append("  ) AND ");
        }
        sql.append("    sales_date < date '" + date + "'-interval'" + valueMonth + " month' ");
        sql.append("    AND sales_date >= date '" + date + "' - interval '1 month' -interval'" + valueMonth + " month' ");
        if (type == 0) {
            sql.append("          AND visit_num = 1 ");
            sql.append("          AND dr.mobile_flag > 0 ");
        }
        if (type == 1) {
            sql.append("     AND visit_num = 1 ");
        }
        if (type == 2) {
            sql.append("      and visit_num in (2,3) ");
        }
        if (type == 3) {
            sql.append("     AND visit_num >= 4 ");
        }
        //sql.append("     AND shop_id = " + shopId + "))*100.0/ ");
        sql.append("     AND view_data_sales_valid.shop_id = " + shopId + "))*100.0/ ");
        
        sql.append("( SELECT count(view_data_sales_valid.customer_id) as count ");
        sql.append("    FROM view_data_sales_valid ");
        if(type == 0) {
            sql.append("    INNER JOIN data_reservation dr  ");
            sql.append("    ON view_data_sales_valid.customer_id = dr.customer_id ");
            sql.append("    AND view_data_sales_valid.shop_id = dr.shop_id ");
            sql.append("    AND view_data_sales_valid.slip_no = dr.slip_no ");
            sql.append("    AND dr.delete_date is null ");
        }
        sql.append("  WHERE   ");
        if (shopCategoryId != 0) {
            sql.append("    exists (");
            sql.append("          (   SELECT 1 ");
            sql.append("                            FROM");
            sql.append("                                data_sales_detail dsd");
            sql.append("                                inner join mst_technic mst on mst.technic_id = dsd.product_id and dsd.product_division = 1");
            sql.append("                                left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
            sql.append("                            WHERE");
            sql.append("                                dsd.shop_id = view_data_sales_valid.shop_id");
            sql.append("                                AND dsd.slip_no = view_data_sales_valid.slip_no");
            sql.append("                                AND dsd.delete_date is null");
            sql.append("                                AND dsd.product_division in (1)");
            sql.append("                                 AND mstc.shop_category_id = " + shopCategoryId + "");
            sql.append("                   )");
            sql.append("        union all");
            sql.append("                        (SELECT  1");
            sql.append("                            FROM");
            sql.append("                                data_sales_detail dsd");
            sql.append("                                inner join mst_item msi on msi.item_id = dsd.product_id and dsd.product_division = 2  ");
            sql.append("                                left join mst_item_class msic on msic.item_class_id = msi.item_class_id ");
            sql.append("                            WHERE");
            sql.append("                                dsd.shop_id = view_data_sales_valid.shop_id");
            sql.append("                                AND dsd.slip_no = view_data_sales_valid.slip_no");
            sql.append("                                AND dsd.delete_date is null");
            sql.append("                                AND dsd.product_division in (2)");
            sql.append("                                 AND msic.shop_category_id =" + shopCategoryId + "");
            sql.append(" )");
            sql.append("  ) AND ");
        }
        // IVS_nahoang end add ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶ 
        
        sql.append("   sales_date < date '" + date + "'-interval'" + valueMonth + " month' ");
        sql.append("          AND sales_date >= date '" + date + "' - interval '1 month' -interval'" + valueMonth + " month' ");
        if (type == 0)
        {
            sql.append("          AND visit_num = 1 ");
            sql.append("          AND dr.mobile_flag > 0 ");
        }
        if (type == 1) {
            sql.append("     AND visit_num = 1 ");
        }
        if (type == 2) {
            sql.append("      and visit_num in (2,3) ");
        }
        if (type == 3) {
            sql.append("     AND visit_num >= 4 ");
        }
        //sql.append("   AND shop_id = " + shopId + ") AS customer_return_rate ");
        sql.append("   AND view_data_sales_valid.shop_id = " + shopId + ") AS customer_return_rate ");
        //IVS_LVTu end edit 2015/01/23 Task #35026
        return sql.toString();
    }
    
    public String getSelectTargetRateSQL(Integer shopId, Integer shopCategoryId, Integer year,int type)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        //IVS_LVTu start add 2015/01/22 Task #35026
        if(type == 0)
        {
            sql.append(" coalesce(repeat_90_netnew,0) as target90, ");
            sql.append(" coalesce(repeat_120_netnew,0) as target120, ");
            sql.append(" coalesce(repeat_180_netnew,0) as target180");
        }
        //IVS_LVTu end add 2015/01/22 Task #35026
        if(type == 1)
        {
            sql.append(" coalesce(repeat_90_new,0) as target90, ");
            sql.append(" coalesce(repeat_120_new,0) as target120, ");
            sql.append(" coalesce(repeat_180_new,0) as target180");
        }
        if(type == 2)
        {
            sql.append(" coalesce(repeat_90_semifix,0) as target90, ");
            sql.append(" coalesce(repeat_120_semifix,0) as target120, ");
            sql.append(" coalesce(repeat_180_semifix,0) as target180");
        }
        if(type == 3)
        {
            sql.append(" coalesce(repeat_90_fix,0) as target90, ");
            sql.append(" coalesce(repeat_120_fix,0) as target120, ");
            sql.append(" coalesce(repeat_180_fix,0) as target180");
        }
        sql.append(" from data_target");
        sql.append(" where shop_id = "+ shopId +"");
        sql.append(" and shop_category_id ="+ shopCategoryId +" ");
        sql.append(" and year ="+ year +"");
        sql.append(" and month =12");
        
        return sql.toString();
    }
    
    public String getSelectReservationRateSQL(Integer shopId,Integer shopCategoryId, Integer year,String date,int type, Integer periodMonth)
    {
        StringBuilder sql = new StringBuilder(1000);
        //IVS_nahoang edit 20141028 
        // [Phase4][Product][Code][Edit]ñ⁄ïWè⁄ç◊ê›íË_ éñëOó\ñÒó¶,éüâÒó\ñÒó¶	PHAN NAY CHUA TINH THEO NENDO( 4~3) 
        sql.append(" select " );
        sql.append("      (select count(dr.reservation_no) from data_reservation dr  " );
        sql.append(" inner join data_reservation_detail drd on drd.shop_id = dr.shop_id and drd.reservation_no= dr.reservation_no " );
        if(shopCategoryId != 0)
        {
            sql.append(" and course_flg is null  " );
            sql.append(" inner join mst_technic mt on mt.technic_id = drd.technic_id " );
            sql.append(" inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id " );
        }
        //sql.append("    inner join data_sales ds on ds.shop_id = ds.shop_id and ds.slip_no = dr.slip_no and ds.customer_id = dr.customer_id    " );
        sql.append("  where  " );
        if( type == 1)
        {
            sql.append(" dr.preorder_flag = 1 and dr.slip_no is not null and  " );
        }
        else
        {
            sql.append(" dr.next_flag = 1 and dr.slip_no is not null and  " );
        }
        sql.append("  dr.delete_date is null and drd.delete_date is null " );
        sql.append("  and dr.shop_id = "+shopId+"  " );
        if(shopCategoryId != 0)
        {
            sql.append("    and mtc.shop_category_id = "+shopCategoryId+" ");
            sql.append("    and drd.delete_date is null ");
        }
        
        if(periodMonth == 12) {
           sql.append("  and date_part('year',drd.reservation_datetime) = date_part('year',date '"+date+"' - interval '1 year' )" ); 
        }else{
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            //sql.append("  and ((date_part('year',drd.reservation_datetime) = date_part('year',date '"+date+"' - interval '1 year' ) and date_part('month',drd.reservation_datetime) > 3) " ); 
            //sql.append("  or (date_part('year',drd.reservation_datetime) = date_part('year',date '"+date+"' ) and date_part('month',drd.reservation_datetime) < 4))" ); 
            sql.append("  and ((date_part('year',drd.reservation_datetime) = date_part('year',date '"+date+"' - interval '1 year' ) and date_part('month',drd.reservation_datetime) >" + periodMonth +") " ); 
            sql.append("  or (date_part('year',drd.reservation_datetime) = date_part('year',date '"+date+"' ) and date_part('month',drd.reservation_datetime) <= " +  periodMonth + "))" ); 
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        }
        
        sql.append("  )*100.0/ " );
        sql.append("     (select count(dr.reservation_no) from data_reservation dr  " );
        sql.append(" inner join data_reservation_detail drd on drd.shop_id = dr.shop_id and drd.reservation_no= dr.reservation_no " );
        if(shopCategoryId != 0)
        {
            sql.append(" and course_flg is null  " );
            sql.append(" inner join mst_technic mt on mt.technic_id = drd.technic_id " );
            sql.append(" inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id " );
        }
        //sql.append("    inner join data_sales ds on ds.shop_id = ds.shop_id and ds.slip_no = dr.slip_no and ds.customer_id = dr.customer_id    " );
        sql.append("  where  " );
        sql.append("  dr.delete_date is null and drd.delete_date is null " );
        sql.append("  and dr.shop_id = "+shopId+"  " );
        if(shopCategoryId != 0)
        {
            sql.append("    and mtc.shop_category_id = "+shopCategoryId+" ");
            sql.append("    and drd.delete_date is null ");
        }
        
        if(periodMonth == 12) {
           sql.append("  and date_part('year',drd.reservation_datetime) = date_part('year',date '"+date+"' - interval '1 year' )" ); 
        }else{
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            //sql.append("  and ((date_part('year',drd.reservation_datetime) = date_part('year',date '"+date+"' - interval '1 year' ) and date_part('month',drd.reservation_datetime) > 3) " ); 
            //sql.append("  or (date_part('year',drd.reservation_datetime) = date_part('year',date '"+date+"' ) and date_part('month',drd.reservation_datetime) < 4))" ); 
            sql.append("  and ((date_part('year',drd.reservation_datetime) = date_part('year',date '"+date+"' - interval '1 year' ) and date_part('month',drd.reservation_datetime) >" + periodMonth +") " ); 
            sql.append("  or (date_part('year',drd.reservation_datetime) = date_part('year',date '"+date+"' ) and date_part('month',drd.reservation_datetime) <= " +  periodMonth + "))" ); 
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        }
        
        sql.append("  )::bigint AS Reservation_rate" );
        //IVS_nahoang end edit.
        
        return sql.toString();
    }
    
    public String getSelectPreviousPeriodSQL(Integer shopId, Integer year,Integer periodMonth, Integer shopCategoryId)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select (((select count(DISTINCT ds.slip_no) from view_data_sales_detail_valid ds ");
        sql.append("   where ds.shop_id =" + shopId + " ");
        if (periodMonth == 12) {
            sql.append("   and ((date_part('year',ds.sales_date) = " + (year - 1) + " ");
            sql.append(" ) )");
        } else {
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            //sql.append("   and ((date_part('year',ds.sales_date) = " + year + " - 1 ");
            //sql.append("   and date_part('month', ds.sales_date) >= 4) ");
            //sql.append("   or (date_part('year',ds.sales_date) = " + year + " ");
            //sql.append("   AND date_part('month', ds.sales_date) <= 3 ))  ");
            sql.append("		AND ((date_part('year',sales_date) = "+year+"-1  " );
            sql.append("		AND date_part('month',sales_date) >"+ periodMonth +") or (date_part('year',sales_date) = "+year+" " );
            sql.append("		AND date_part('month',sales_date) <=" + periodMonth +")) " );
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        }
                // IVS_nahoang start add ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶  20141027
        if(shopCategoryId != 0){
            sql.append(" AND exists ((SELECT 1");
            sql.append("            FROM");
            sql.append("            data_sales_detail dsd");
            sql.append("            inner join mst_technic mst on mst.technic_id = dsd.product_id and dsd.product_division = 1");
            sql.append("            left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
            sql.append("        WHERE");
            sql.append("            dsd.shop_id = ds.shop_id");
            sql.append("            AND dsd.slip_no = ds.slip_no");
            sql.append("            AND dsd.delete_date is null");
            sql.append("            AND dsd.product_division in (1)");
            sql.append("             AND mstc.shop_category_id = "+ shopCategoryId +"");
            sql.append("  )");
            sql.append("             union all");
            sql.append("             (SELECT 1 ");
            sql.append("        FROM");
            sql.append("            data_sales_detail dsd");
            sql.append("            inner join mst_item msi on msi.item_id = dsd.product_id and dsd.product_division = 2");
            sql.append("            left join mst_item_class msic on msic.item_class_id = msi.item_class_id ");
            sql.append("        WHERE");
            sql.append("            dsd.shop_id = ds.shop_id");
            sql.append("            AND dsd.slip_no = ds.slip_no");
            sql.append("            AND dsd.delete_date is null");
            sql.append("            AND dsd.product_division in (2)");
            sql.append("             AND msic.shop_category_id = "+ shopCategoryId +"");  
            sql.append("      )  )");
        }
            
        // IVS_nahoang end add ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶    
        sql.append("   AND ds.visit_num = 1) *100.0)) ");
        sql.append(" / ");
        sql.append(" (select count(DISTINCT ds.slip_no) from view_data_sales_detail_valid ds ");
        sql.append("   where ds.shop_id =" + shopId + " ");
        if (periodMonth == 12) {
            sql.append("   and ((date_part('year',ds.sales_date) = " + (year -1) + " ");
            sql.append(" ) )");
        } else {
            sql.append("   and ((date_part('year',ds.sales_date) = " + (year -1) + " ");
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            sql.append("   and date_part('month', ds.sales_date) >"+ periodMonth + ") ");
            sql.append(" or (date_part('year',ds.sales_date) =" + year + " ");
            sql.append(" AND date_part('month', ds.sales_date) <= "+ periodMonth +")) ");
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        }

                // IVS_nahoang start add ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶  20141027
        if(shopCategoryId != 0){
            sql.append(" AND exists ((SELECT 1");
            sql.append("            FROM");
            sql.append("            data_sales_detail dsd");
            sql.append("            inner join mst_technic mst on mst.technic_id = dsd.product_id and dsd.product_division = 1");
            sql.append("            left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
            sql.append("        WHERE");
            sql.append("            dsd.shop_id = ds.shop_id");
            sql.append("            AND dsd.slip_no = ds.slip_no");
            sql.append("            AND dsd.delete_date is null");
            sql.append("            AND dsd.product_division in (1)");
            sql.append("             AND mstc.shop_category_id = "+ shopCategoryId +"");
            sql.append("  )");
            sql.append("             union all");
            sql.append("             (SELECT 1 ");
            sql.append("        FROM");
            sql.append("            data_sales_detail dsd");
            sql.append("            inner join mst_item msi on msi.item_id = dsd.product_id and dsd.product_division = 2");
            sql.append("            left join mst_item_class msic on msic.item_class_id = msi.item_class_id ");
            sql.append("        WHERE");
            sql.append("            dsd.shop_id = ds.shop_id");
            sql.append("            AND dsd.slip_no = ds.slip_no");
            sql.append("            AND dsd.delete_date is null");
            sql.append("            AND dsd.product_division in (2)");
            sql.append("             AND msic.shop_category_id = "+ shopCategoryId +"");  
            sql.append("      )  )");
        }
            
        // IVS_nahoang end add ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶    
        sql.append(" ) as previous_period");

        return sql.toString();
    }
   
    // IVS_nahoang start add ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶ 20141027
    //[Phase4][Product][Code][Edit]ñ⁄ïWè⁄ç◊ê›íË_ÅÉêVãKî‰ó¶ñ⁄ïWê›íËÅÑ dang tinh chua dung
    public Integer getSelectPeriodPeople(Integer shopId, Integer year,Integer periodMonth) throws SQLException{
        StringBuilder builder = new StringBuilder();
        builder.append(" select count(DISTINCT ds.slip_no) from view_data_sales_detail_valid ds ");
        builder.append("   where ds.shop_id =" + shopId + " ");
        if (periodMonth == 12) {
            builder.append("   and ((date_part('year',ds.sales_date) = " + (year - 1) + " ");
        } else {
            builder.append("   and ((date_part('year',ds.sales_date) = " + year + " - 1 ");
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            builder.append("   and date_part('month', ds.sales_date) > " + periodMonth + ") ");
            builder.append("   or (date_part('year',ds.sales_date) = " + year + " ");
            builder.append("   AND date_part('month', ds.sales_date) <= " + periodMonth + " ");
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        }
        builder.append("   )) ");
        builder.append("   AND ds.visit_num = 1 ");
        
        ConnectionWrapper con = SystemInfo.getConnection();
        Integer numberOfPeriod = 0;
         try {
            ResultSetWrapper rs = con.executeQuery(builder.toString());
            while (rs.next()) {
                numberOfPeriod = rs.getInt("count");
            }
         }catch (SQLException ex) {
            Logger.getLogger(DataTargets.class.getName()).log(Level.SEVERE, null, ex);
         }
//        }finally{
//             con.close();
//         }
         return numberOfPeriod;
    }
    // IVS_nahoang end add ñ⁄ïWè⁄ç◊ê›íË_äeçƒóàó¶ 20141027
    
    public String getSelectTargetNewRateSQL(Integer shopId, Integer shopCategoryId, Integer year)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select new_rate");
        sql.append(" from data_target");
        sql.append(" where shop_id ="+shopId+"");
        sql.append(" and shop_category_id ="+shopCategoryId+" ");
        sql.append(" and year ="+year+"");
        sql.append(" and month =12 ");
        
        return sql.toString();
    }
    
    
    public String getSelectDataTargetMotiveSQL(Integer shopId, Integer shopCategoryId, String date, Integer firstComingMotiveClassId)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select ");
        sql.append(" b.year,b.month, ");
        sql.append(" ( ");
        sql.append(" select dtm.motive_id ");
        sql.append(" from data_target_motive dtm ");
        sql.append(" where dtm.shop_id =" + shopId + " ");
        sql.append(" and dtm.shop_category_id =" + shopCategoryId + " ");
        sql.append(" and dtm.year = b.year and dtm.month = b.month ");
        sql.append(" and dtm.motive_id =" + firstComingMotiveClassId + " ");
        sql.append(" ) as motive_id , ");
        sql.append(" coalesce( ");
        sql.append(" (select coalesce(num,0) ");
        sql.append(" from data_target_motive dtm ");
        sql.append(" where dtm.shop_id =" + shopId + " ");
        sql.append(" and dtm.shop_category_id = " + shopCategoryId + " ");
        sql.append(" and dtm.year = b.year and dtm.month = b.month ");
        sql.append(" and dtm.motive_id =" + firstComingMotiveClassId + " ");
        sql.append(" ),0) as num_value ");
        //IVS_LVTu start add 2015/01/22 Task #35026
        sql.append(", coalesce( ");
        sql.append(" (select coalesce(own_flg,0) ");
        sql.append(" from data_target_motive dtm ");
        sql.append(" where dtm.shop_id =" + shopId + " ");
        sql.append(" and dtm.shop_category_id = " + shopCategoryId + " ");
        sql.append(" and dtm.year = b.year and dtm.month = b.month ");
        sql.append(" and dtm.motive_id =" + firstComingMotiveClassId + " ");
        sql.append(" ),0) as own_flg ");
        //IVS_LVTu end add 2015/01/22 Task #35026
        sql.append(" from ");
        sql.append(" ( ");
        sql.append(" 	select date_part('year',a.date) as year,date_part('month',a.date) as month ");
        sql.append(" 	from ");
        sql.append(" 	( ");
        sql.append(" 	select date'" + date + "' +interval'0 month'as date ");
        sql.append(" 	union ");
        sql.append(" 	select date '" + date + "'+interval'1 month' as date ");
        sql.append(" 	union ");
        sql.append(" 	select date '" + date + "'+interval'2 month' as date ");
        sql.append(" 	union ");
        sql.append(" 	select date '" + date + "'+interval'3 month' as date ");
        sql.append(" 	union ");
        sql.append(" 	select date '" + date + "'+interval'4 month' as date ");
        sql.append(" 	union ");
        sql.append(" 	select date '" + date + "'+interval'5 month' as date ");
        sql.append(" 	union ");
        sql.append(" 	select date '" + date + "'+interval'6 month' as date ");
        sql.append(" 	union ");
        sql.append(" 	select date '" + date + "'+interval'7 month' as date ");
        sql.append(" 	union ");
        sql.append(" 	select date '" + date + "'+interval'8 month' as date ");
        sql.append(" 	union ");
        sql.append(" 	select date '" + date + "'+interval'9 month' as date ");
        sql.append(" 	union ");
        sql.append(" 	select date '" + date + "'+interval'10 month' as date ");
        sql.append(" 	union ");
        sql.append(" 	select date '" + date + "'+interval'11 month' as date ");
        sql.append(" 	)a ");
        sql.append(" )b ");
        sql.append(" order by b.year,b.month ");

        return sql.toString();
    }

    public String getSelectSetTargetRateMenuSQL(Integer shopId,Integer shopCategoryId, Integer year,Integer periodMonth)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" SELECT mtc.technic_class_id , " );
        sql.append(" 	   mtc.technic_class_name , " );
        sql.append(" 	   coalesce(ds.sales_value, 0) AS sales_value , " );
        sql.append(" 	   coalesce(ds.customer_count, 0) AS customer_count, " );
        
        sql.append(" coalesce((SELECT  " );
        sql.append(" count( distinct ds.slip_no) AS customer_count " );
        sql.append("   FROM view_data_sales_detail_valid ds " );
        if(shopCategoryId != 0)
        {
            sql.append(" 	LEFT JOIN mst_technic mt ON mt.technic_id = ds.product_id " );
            sql.append(" 	LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mt.technic_class_id " );
        }
        sql.append("    WHERE ds.product_division IN (1) " );
        if(shopCategoryId != 0)
        {
            sql.append("    AND mtc.shop_category_id = "+shopCategoryId+" " );
        }
        sql.append(" 	AND ds.shop_id = "+shopId+" " );
        if(periodMonth == 12)
        {
            sql.append(" 	AND ds.sales_date BETWEEN '"+(year-1)+"-01-01' AND '"+(year-1)+"-12-31' " );
        }
        else
        {
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            //Integer preYear = year - 1;
            //sql.append(" 	 AND ds.sales_date BETWEEN '"+preYear+"-04-01' AND '"+year+"-03-31' " );
            sql.append("		AND ((date_part('year',sales_date) = "+year+"-1  " );
            sql.append("		AND date_part('month',sales_date) >"+ periodMonth +") or (date_part('year',sales_date) = "+year+" " );
            sql.append("		AND date_part('month',sales_date) <=" + periodMonth +")) " );
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        }
        sql.append("    GROUP BY ds.shop_id),0) as total_num " );
        
        
        sql.append(" FROM mst_technic_class mtc " );
        sql.append(" LEFT JOIN " );
        sql.append("   (SELECT mt.technic_class_id , " );
        sql.append(" 		  sum(discount_detail_value_no_tax) AS sales_value , " );
        sql.append(" 		  count(DISTINCT dsd.slip_no) AS customer_count " );
        sql.append("    FROM view_data_sales_detail_valid dsd " );
        sql.append("    LEFT JOIN mst_technic mt ON mt.technic_id = dsd.product_id " );
        if(shopCategoryId != 0)
        {
            sql.append("    LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mt.technic_class_id " );
        }
        sql.append("    WHERE dsd.product_division IN (1) " );
        if(shopCategoryId != 0)
        {
            sql.append(" AND mtc.shop_category_id = "+shopCategoryId+"  " );
        }
        sql.append(" 	 AND dsd.shop_id = "+shopId+" " );
        if(periodMonth == 12)
        {
            sql.append(" 	 AND dsd.sales_date BETWEEN '"+(year-1)+"-01-01' AND '"+(year-1)+"-12-31' " );
        }
        else
        {
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            //Integer preYear = year - 1;
            //sql.append(" 	 AND dsd.sales_date BETWEEN '"+preYear+"-04-01' AND '"+year+"-03-31' " );
            sql.append("		AND ((date_part('year',sales_date) = "+year+"-1  " );
            sql.append("		AND date_part('month',sales_date) >"+ periodMonth +") or (date_part('year',sales_date) = "+year+" " );
            sql.append("		AND date_part('month',sales_date) <=" + periodMonth +")) " );
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        }
        sql.append("    GROUP BY mt.technic_class_id) ds ON ds.technic_class_id = mtc.technic_class_id " );
        sql.append(" WHERE mtc.delete_date IS NULL " );
        sql.append("   AND mtc.prepaid = 0 " );
        sql.append("   AND EXISTS " );
        sql.append(" 	(SELECT 1 " );
        sql.append(" 	 FROM mst_use_product mup " );
        sql.append(" 	 INNER JOIN mst_technic mt ON mup.product_id = mt.technic_id " );
        sql.append(" 	 WHERE mup.shop_id ="+shopId+" " );
        sql.append(" 	   AND mup.product_division = 1 " );
        sql.append(" 	   AND mt.technic_class_id = mtc.technic_class_id) " );
        //IVS_LVTu start add 2014/12/02 Mashu_ñ⁄ïWä«óùâÊñ  Task #33427
        if(shopCategoryId != 0)
        {
            sql.append(" 	 and mtc.technic_class_id in (SELECT technic_class_id FROM mst_technic_class " );
            sql.append(" 	   WHERE shop_category_id = "+ shopCategoryId +" " );
            sql.append(" 	   and delete_date is null) " );
        }
        //IVS_LVTu end add 2014/12/02 Mashu_ñ⁄ïWä«óùâÊñ  Task #33427
        sql.append(" ORDER BY mtc.display_seq ");
        
        return sql.toString();
    }
    
    public String getSelectSetProductTargetSQL(Integer shopId,Integer shopCategoryId, Integer year,Integer periodMonth)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" SELECT mgc.item_class_id , " );
        sql.append(" 	   mgc.item_class_name , " );
        sql.append(" 	   coalesce(ds.sales_value, 0) AS sales_value  " );
        sql.append(" FROM mst_item_class mgc " );
        sql.append(" LEFT JOIN " );
        sql.append("   (SELECT mg.item_class_id , " );
        sql.append(" 		  sum(discount_detail_value_no_tax) AS sales_value  " );
        sql.append("    FROM view_data_sales_detail_valid dsd " );
        sql.append("    LEFT JOIN mst_item mg ON mg.item_id = dsd.product_id " );
        if(shopCategoryId != 0)
        {
            sql.append("    LEFT JOIN mst_item_class mic ON mg.item_class_id = mic.item_class_id " );
        }
        sql.append("    WHERE dsd.product_division IN (2) " );
        if(shopCategoryId != 0)
        {
            sql.append(" AND mic.shop_category_id = "+shopCategoryId+"  " );
        }
        sql.append(" 	 AND dsd.shop_id = "+shopId+" " );
        if(periodMonth == 12)
        {
            sql.append(" 	 AND dsd.sales_date BETWEEN '"+(year - 1)+"/01/01' AND '"+(year - 1)+"/12/31' " );
        }
        else
        {
            //IVS_LVTu start edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
            //Integer preYear = year - 1;
            //sql.append(" 	 AND (dsd.sales_date BETWEEN '"+preYear+"/04/01' AND '"+year+"/03/31' " );
            sql.append("		AND ((date_part('year',sales_date) = "+year+"-1  " );
            sql.append("		AND date_part('month',sales_date) >"+ periodMonth +") or (date_part('year',sales_date) = "+year+" " );
            sql.append("		AND date_part('month',sales_date) <=" + periodMonth +")) " );
            //IVS_LVTu end edit 2014/12/03 Mashu_ê›íËâÊñ  change request ä˙ññåé (1åéÅ`ÇPÇQåé)
        }
        sql.append("    GROUP BY mg.item_class_id) ds ON ds.item_class_id = mgc.item_class_id " );
        sql.append(" WHERE mgc.delete_date IS NULL " );
        sql.append("   AND EXISTS " );
        sql.append(" 	(SELECT 1 " );
        sql.append(" 	 FROM mst_use_product mup " );
        sql.append(" 	 INNER JOIN mst_item mi ON mup.product_id = mi.item_id " );
        sql.append(" 	 WHERE mup.shop_id = "+shopId+" " );
        sql.append(" 	   AND mup.product_division = 2 " );
        sql.append(" 	   AND mi.item_class_id = mgc.item_class_id) " );
        //IVS_LVTu start add 2014/12/02 Mashu_ñ⁄ïWä«óùâÊñ  Task #33427
        if(shopCategoryId != 0)
        {
            sql.append(" 	 and mgc.item_class_id in (SELECT item_class_id FROM mst_item_class " );
            sql.append(" 	   WHERE shop_category_id = "+ shopCategoryId +" " );
            sql.append(" 	   and delete_date is null) " );
        }
        //IVS_LVTu end add 2014/12/02 Mashu_ñ⁄ïWä«óùâÊñ  Task #33427
        sql.append(" ORDER BY  mgc.display_seq");
        
        return sql.toString();
    }
    
    //IVS_LVTu start edit 2015/06/02 New request #37146
    public String getSqlRepeat(int shopID,int SelectedYear,int SelectedMonth,int shopCategoryId,int monthCount) {
        
        StringBuilder sql = new StringBuilder(1000);
        
        String StartDay = ""+ SelectedYear +"/"+ SelectedMonth +"/01";
        
        sql.append(" SELECT  ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num = 1 THEN 1 ELSE 0 END), 0) AS new_total, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num = 1 ");
        sql.append(" 					AND EXISTS ");
        sql.append(" 					  (SELECT 1 ");
        sql.append(" 					   FROM ");
        sql.append(" 						 (SELECT ds.sales_date,ds.customer_id,ds.insert_date ");
        sql.append(" 						  FROM data_sales ds ");
        sql.append(" 						  JOIN mst_customer mc using(customer_id) ");
        sql.append(" 						  WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append(" 							  And EXISTS(SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND product_division IN (1) ");
        sql.append(" 								 AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 							   UNION ALL SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND dsd.product_division IN (5,6) ");
        sql.append(" 								 AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 							AND ds.delete_date IS NULL ");
        sql.append(" 							AND mc.customer_no <> '0' ");
        sql.append(" 							AND ds.shop_id IN ("+ shopID +") ");
        sql.append(" 							AND ds.sales_date BETWEEN date ' "+ StartDay +"' - interval ' "+ monthCount + " month' AND date' " + StartDay + "'+interval '1 month' -interval '1 day') w ");
        sql.append(" 					   WHERE w.customer_id = t.customer_id ");
        sql.append(" 						 AND (w.sales_date > t.sales_date ");
        sql.append(" 							  OR (w.sales_date = t.sales_date ");
        sql.append(" 								  AND w.insert_date > t.insert_date))) THEN 1 ELSE 0 END), 0) AS new_reappearance, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num BETWEEN 2 AND 3 THEN 1 ELSE 0 END), 0) AS sub_fixed_total, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num BETWEEN 2 AND 3 ");
        sql.append(" 					AND EXISTS ");
        sql.append(" 					  (SELECT 1 ");
        sql.append(" 					   FROM ");
        sql.append(" 						 (SELECT ds.sales_date,ds.customer_id,ds.insert_date ");
        sql.append(" 						  FROM data_sales ds ");
        sql.append(" 						  JOIN mst_customer mc using(customer_id) ");
        sql.append(" 						  WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append(" 							  And EXISTS(SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND product_division IN (1) ");
        sql.append(" 								 AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 							   UNION ALL SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 							AND ds.delete_date IS NULL ");
        sql.append(" 							AND mc.customer_no <> '0' ");
        sql.append(" 							AND ds.shop_id IN ("+ shopID +") ");
        sql.append(" 							AND ds.sales_date BETWEEN date' "+ StartDay +"' - interval ' "+ monthCount + " month' AND date' " + StartDay + "'+interval '1 month' -interval '1 day') w ");
        sql.append(" 					   WHERE w.customer_id = t.customer_id ");
        sql.append(" 						 AND (w.sales_date > t.sales_date ");
        sql.append(" 							  OR (w.sales_date = t.sales_date ");
        sql.append(" 								  AND w.insert_date > t.insert_date))) THEN 1 ELSE 0 END), 0) AS sub_fixed_reappearance, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num >= 4 THEN 1 ELSE 0 END), 0) AS fixed_total, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num >= 4 ");
        sql.append(" 					AND EXISTS ");
        sql.append(" 					  (SELECT 1 ");
        sql.append(" 					   FROM ");
        sql.append(" 						 (SELECT ds.sales_date,ds.customer_id,ds.insert_date ");
        sql.append(" 						  FROM data_sales ds ");
        sql.append(" 						  JOIN mst_customer mc using(customer_id) ");
        sql.append(" 						  WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append(" 							  And EXISTS(SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND product_division IN (1) ");
        sql.append(" 								 AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 							   UNION ALL SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND dsd.product_division IN (5,6) ");
        sql.append(" 								 AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 							AND ds.delete_date IS NULL ");
        sql.append(" 							AND mc.customer_no <> '0' ");
        sql.append(" 							AND ds.shop_id IN ("+ shopID +") ");
        sql.append(" 							AND ds.sales_date BETWEEN date' "+ StartDay +"' - interval ' "+ monthCount + " month' AND date' " + StartDay + "'+interval '1 month' -interval '1 day') w ");
        sql.append(" 					   WHERE w.customer_id = t.customer_id ");
        sql.append(" 						 AND (w.sales_date > t.sales_date ");
        sql.append(" 							  OR (w.sales_date = t.sales_date ");
        sql.append(" 								  AND w.insert_date > t.insert_date))) THEN 1 ELSE 0 END), 0) AS fixed_reappearance ");
        sql.append(" FROM ");
        sql.append("   (SELECT DISTINCT ds.sales_date ,ds.customer_id ,ds.insert_date ,mc.introducer_id ,mc.birthday ,mc.sex , ");
        sql.append(" 	 (SELECT count(slip_no) + coalesce(max(before_visit_num),0) ");
        sql.append(" 	  FROM data_sales ");
        sql.append(" 	  INNER JOIN mst_customer using(customer_id) ");
        sql.append(" 						  WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append(" 							  And EXISTS(SELECT 1 ");
        sql.append(" 		   FROM data_sales_detail dsd ");
        sql.append(" 		   INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 		   AND dsd.product_division = 1 ");
        sql.append(" 		   LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
        sql.append(" 		   WHERE dsd.shop_id = data_sales.shop_id ");
        sql.append(" 			 AND dsd.slip_no = data_sales.slip_no ");
        sql.append(" 			 AND dsd.delete_date IS NULL ");
        sql.append(" 			 AND dsd.product_division IN (1) ");
        sql.append(" 			 AND mstc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 		   UNION ALL SELECT 1 ");
        sql.append(" 		   FROM data_sales_detail dsd ");
        sql.append(" 		   INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 		   AND dsd.product_division IN (5, ");
        sql.append(" 										6) ");
        sql.append(" 		   LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id ");
        sql.append(" 		   WHERE dsd.shop_id = data_sales.shop_id ");
        sql.append(" 			 AND dsd.slip_no = data_sales.slip_no ");
        sql.append(" 			 AND dsd.delete_date IS NULL ");
        sql.append(" 			 AND dsd.product_division IN (5, ");
        sql.append(" 										  6) ");
        sql.append(" 			 AND mscc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 		AND data_sales.delete_date IS NULL ");
        sql.append(" 		AND customer_id = ds.customer_id ");
        sql.append(" 		AND data_sales.shop_id= ds.shop_id ");
        sql.append(" 		AND (data_sales.sales_date < ds.sales_date ");
        sql.append(" 			 OR (data_sales.sales_date = ds.sales_date ");
        sql.append(" 				 AND data_sales.insert_date <= ds.insert_date))) AS visit_num ");
        sql.append("    FROM data_sales ds ");
        sql.append("    JOIN mst_customer mc using(customer_id) ");
        sql.append(" 						  WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append("            And EXISTS(SELECT 1 ");
        sql.append(" 		FROM data_sales_detail dsd ");
        sql.append(" 		INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 		LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
        sql.append(" 		WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 		  AND dsd.slip_no = ds.slip_no ");
        sql.append(" 		  AND dsd.delete_date IS NULL ");
        sql.append(" 		  AND product_division IN (1) ");
        sql.append(" 		  AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 		UNION ALL SELECT 1 ");
        sql.append(" 		FROM data_sales_detail dsd ");
        sql.append(" 		INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 		LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
        sql.append(" 		WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 		  AND dsd.slip_no = ds.slip_no ");
        sql.append(" 		  AND dsd.delete_date IS NULL ");
        sql.append(" 		  AND dsd.product_division IN (5, 6)  ");
        sql.append(" 		  AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 	 AND ds.delete_date IS NULL ");
        sql.append(" 	 AND mc.customer_no <> '0' ");
        sql.append(" 	 AND ds.shop_id IN ("+ shopID +") ");
        sql.append(" 	 AND ds.sales_date BETWEEN date' "+ StartDay +"' - interval ' "+ monthCount + " month' AND date' " + StartDay + "'- interval '" + (monthCount - 1) +" month' -interval '1 day') t ");
        
        return sql.toString();
    }
    
    
    public String sqlQueryMobileRepeat(int shopID,int SelectedYear,int SelectedMonth,int shopCategoryId,int monthCount) {
        
        StringBuilder sql = new StringBuilder(1000);
        
        String StartDay = ""+ SelectedYear +"/"+ SelectedMonth +"/01";
        
        sql.append(" SELECT  ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num = 1 and mobile_flag >0 THEN 1 ELSE 0 END), 0) AS net_new_total, ");
        sql.append(" 	   coalesce(sum(CASE WHEN visit_num = 1 and mobile_flag >0 ");
        sql.append(" 					AND EXISTS ");
        sql.append(" 					  (SELECT 1 ");
        sql.append(" 					   FROM ");
        sql.append(" 						 (SELECT ds.sales_date,ds.customer_id,ds.insert_date ");
        sql.append(" 						  FROM data_sales ds ");
        sql.append(" 						  JOIN mst_customer mc using(customer_id) ");
       sql.append(" 						  WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append("                                                       And EXISTS(SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND product_division IN (1) ");
        sql.append(" 								 AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 							   UNION ALL SELECT 1 ");
        sql.append(" 							   FROM data_sales_detail dsd ");
        sql.append(" 							   INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 							   LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
        sql.append(" 							   WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 								 AND dsd.slip_no = ds.slip_no ");
        sql.append(" 								 AND dsd.delete_date IS NULL ");
        sql.append(" 								 AND dsd.product_division IN (5,6) ");
        sql.append(" 								 AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 							AND ds.delete_date IS NULL ");
        sql.append(" 							AND mc.customer_no <> '0' ");
        sql.append(" 							AND ds.shop_id IN ("+ shopID +") ");
        sql.append(" 							AND ds.sales_date BETWEEN date ' "+ StartDay +"' - interval ' "+ monthCount + " month' AND date' " + StartDay + "'+interval '1 month' -interval '1 day') w ");
        sql.append(" 					   WHERE w.customer_id = t.customer_id ");
        sql.append(" 						 AND (w.sales_date > t.sales_date ");
        sql.append(" 							  OR (w.sales_date = t.sales_date ");
        sql.append(" 								  AND w.insert_date > t.insert_date))) THEN 1 ELSE 0 END), 0) AS net_new_reappearance ");
        sql.append(" FROM ");
        sql.append("   (SELECT DISTINCT ds.sales_date ,ds.customer_id ,ds.insert_date ,mc.introducer_id ,mc.birthday ,mc.sex ,dr.mobile_flag, ");
        sql.append(" 	 (SELECT count(slip_no) + coalesce(max(before_visit_num),0) ");
        sql.append(" 	  FROM data_sales ");
        sql.append(" 	  INNER JOIN mst_customer using(customer_id) ");
       sql.append("       WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append("               And EXISTS(SELECT 1 ");
        sql.append(" 		   FROM data_sales_detail dsd ");
        sql.append(" 		   INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 		   AND dsd.product_division = 1 ");
        sql.append(" 		   LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
        sql.append(" 		   WHERE dsd.shop_id = data_sales.shop_id ");
        sql.append(" 			 AND dsd.slip_no = data_sales.slip_no ");
        sql.append(" 			 AND dsd.delete_date IS NULL ");
        sql.append(" 			 AND dsd.product_division IN (1) ");
        sql.append(" 			 AND mstc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 		   UNION ALL SELECT 1 ");
        sql.append(" 		   FROM data_sales_detail dsd ");
        sql.append(" 		   INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 		   AND dsd.product_division IN (5, ");
        sql.append(" 										6) ");
        sql.append(" 		   LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id ");
        sql.append(" 		   WHERE dsd.shop_id = data_sales.shop_id ");
        sql.append(" 			 AND dsd.slip_no = data_sales.slip_no ");
        sql.append(" 			 AND dsd.delete_date IS NULL ");
        sql.append(" 			 AND dsd.product_division IN (5, ");
        sql.append(" 										  6) ");
        sql.append(" 			 AND mscc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 		AND data_sales.delete_date IS NULL ");
        sql.append(" 		AND customer_id = ds.customer_id ");
        sql.append(" 		AND data_sales.shop_id= ds.shop_id ");
        sql.append(" 		AND (data_sales.sales_date < ds.sales_date ");
        sql.append(" 			 OR (data_sales.sales_date = ds.sales_date ");
        sql.append(" 				 AND data_sales.insert_date <= ds.insert_date))) AS visit_num ");
        sql.append("    FROM data_sales ds ");
        sql.append("     inner join data_reservation dr on ds.slip_no= dr.slip_no and ds.shop_id = dr.shop_id ");
        sql.append("    JOIN mst_customer mc  on ds.customer_id = mc.customer_id  ");
        sql.append("    WHERE 1=1 ");
        if (shopCategoryId > 0) {
        sql.append("    And EXISTS(SELECT 1 ");
        sql.append(" 		FROM data_sales_detail dsd ");
        sql.append(" 		INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
        sql.append(" 		LEFT JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
        sql.append(" 		WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 		  AND dsd.slip_no = ds.slip_no ");
        sql.append(" 		  AND dsd.delete_date IS NULL ");
        sql.append(" 		  AND product_division IN (1) ");
        sql.append(" 		  AND mtc.shop_category_id IN ("+ shopCategoryId + ") ");
        sql.append(" 		UNION ALL SELECT 1 ");
        sql.append(" 		FROM data_sales_detail dsd ");
        sql.append(" 		INNER JOIN mst_course msc ON msc.course_id = dsd.product_id ");
        sql.append(" 		LEFT JOIN mst_course_class mcc ON mcc.course_class_id = msc.course_class_id ");
        sql.append(" 		WHERE dsd.shop_id = ds.shop_id ");
        sql.append(" 		  AND dsd.slip_no = ds.slip_no ");
        sql.append(" 		  AND dsd.delete_date IS NULL ");
        sql.append(" 		  AND dsd.product_division IN (5, 6)  ");
        sql.append(" 		  AND mcc.shop_category_id IN ("+ shopCategoryId + ")) ");
        }
        sql.append(" 	 AND ds.delete_date IS NULL ");
        sql.append(" 	 AND mc.customer_no <> '0' ");
        sql.append(" 	 AND ds.shop_id IN ("+ shopID +") ");
        sql.append(" 	 AND ds.sales_date BETWEEN date' "+ StartDay +"' - interval ' "+ monthCount + " month' AND date' " + StartDay + "'- interval '" + (monthCount - 1) +" month' -interval '1 day') t ");
        
        return sql.toString();
    }
    //IVS_LVTu end edit 2015/06/02 New request #37146
    
    public MstDataTarget loadTargetkarte (ConnectionWrapper con,Integer shopId,Integer shopCategoryId, Integer year) throws SQLException
    {
        MstDataTarget dt = new MstDataTarget();
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select karte1_day, karte1_value, karte2_day, karte2_value ");
        sql.append(" ,karte3_day, karte3_value, karte4_day, karte4_value ");
        sql.append(" from data_target ");
        sql.append(" where shop_id ="+shopId+" ");
        sql.append(" and shop_category_id ="+shopCategoryId+" ");
        sql.append(" and month = 12 ");
        sql.append(" and year ="+year+" ");
        
        ResultSetWrapper	rs	=	con.executeQuery( sql.toString());
        if( rs.next())
        {
            dt.setKarte1_day(rs.getInt("karte1_day"));
            dt.setKarte1_value(rs.getInt("karte1_value"));
            dt.setKarte2_day(rs.getInt("karte2_day"));
            dt.setKarte2_value(rs.getInt("karte2_value"));
            dt.setKarte3_day(rs.getInt("karte3_day"));
            dt.setKarte3_value(rs.getInt("karte3_value"));
            dt.setKarte4_day(rs.getInt("karte4_day"));
            dt.setKarte4_value(rs.getInt("karte4_value"));
        }
        rs.close();
        return dt;
    }
}

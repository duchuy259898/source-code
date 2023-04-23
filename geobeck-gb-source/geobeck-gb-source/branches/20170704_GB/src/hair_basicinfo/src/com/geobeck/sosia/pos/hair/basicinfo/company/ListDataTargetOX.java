/*
 * ListDataTargetOX.java
 *
 * Created on 2013/04/25, 17:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.basicinfo.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import java.util.logging.Level;

/**
 * スタッフ区分データ
 *
 * @author nakhoa
 */
public class ListDataTargetOX extends ArrayList<DataTargetOX> {

    static final protected int MONTHS_COUNT = 12;

    /**
     * 
     */
    public ListDataTargetOX() {
    }
    
    public void loadPerformance(ConnectionWrapper con,int shopID , int year) throws SQLException {
        for (int i = 1; i <= 12; i++) {
            ResultSetWrapper rs = con.executeQuery(getSelectPerformanceSQL(year, i,shopID));
            while (rs.next()) {
                DataTargetOX msc = new DataTargetOX();
                msc.clear();
                msc.setData(rs);
                this.add(msc);
            }
            rs.close();
        }
    }

    private String getSelectPerformanceSQL(Integer year, Integer month, Integer shopID ) {
        StringBuilder sql = new StringBuilder();
        sql.append("Select \n");
        sql.append("    tb1.technic, tb1.item, tb2.cota , tb3.cut_num , tb3.cut_sale \n");
        sql.append("    ,tb3.perm_num, tb3.perm_sale, tb3.col_num, tb3.col_sale, tb3.spa_num"
                + "     \n,tb3.spa_sale ,tb3.treat_num,tb3.treat_sale, tb3.item_num \n");
        sql.append("    ,tb4.y_num, tb4.m_num, tb4.o_num, tb4.un_num, tb5.new_num \n");
        sql.append("    , tb5.usally_num, tb5.new_repert_num, tb6.intro_num, tb1.nominat_num \n");
        sql.append(" from \n");
        sql.append("     (\n");
        sql.append("            select \n");
        sql.append("            sum(case when  product_division = 1 then product_value else 0 end ) as technic \n");
        sql.append("           , sum(case when  product_division = 2 then product_value else 0 end ) as item \n");
        sql.append("            , count(case when ds.designated_flag = true then 1 else null end ) as nominat_num ");
        sql.append("           from data_sales ds  \n");
        sql.append("           inner join data_sales_detail  dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no   \n");
        sql.append("           where ds.shop_id =  ").append(SQLUtil.convertForSQL(shopID));
        sql.append("           and date_part('year',ds.sales_date) =   ").append(SQLUtil.convertForSQL(year));
        sql.append("           and date_part('month',ds.sales_date) =  ").append(SQLUtil.convertForSQL(month));
        sql.append(") tb1 \n");
        sql.append(", ( \n ");
        sql.append("            select sum(product_value) as cota \n");
        sql.append("           from \n");
        sql.append("            (  		select dsd1.product_value,dsd1.product_id \n");
        sql.append("                            from data_sales ds1 \n");
        sql.append("                             inner join data_sales_detail as dsd1 on ds1.shop_id = dsd1.shop_id  and ds1.slip_no = dsd1.slip_no \n ");
        sql.append("                             where product_division = 2 \n ");
        sql.append("                             and ds1.shop_id = ").append(SQLUtil.convertForSQL(shopID)).append("\n");
        sql.append("                             and date_part('year',ds1.sales_date) = ").append(SQLUtil.convertForSQL(year)).append("\n");
        sql.append("                             and date_part('month',ds1.sales_date) =  ").append(SQLUtil.convertForSQL(month)).append("\n");
        sql.append("              )ds3 inner join \n");
        sql.append("              (	select mi.item_id \n");
        sql.append("                     from mst_item  mi inner join mst_item_class  mic on mi.item_class_id = mic.item_class_id \n");
        sql.append("                     where mic.item_integration_id = 1 \n");
        sql.append("              ) mi1 on mi1.item_id = ds3.product_id  \n");
        sql.append("             ) tb2 \n");
        sql.append("  ,( \n");
        sql.append("        select count(case when technic_integration_id =1 then product_value else null end) as cut_num \n");
        sql.append("       , sum (case when technic_integration_id =1 then product_value else 0 end) as cut_sale \n");
        sql.append("       ,count(case when technic_integration_id =2 then product_value else null end) as perm_num \n");
        sql.append("       , sum (case when technic_integration_id =2 then product_value else 0 end) as perm_sale \n");
        sql.append("       ,count(case when technic_integration_id =3 then product_value else null end) as col_num \n");
        sql.append("       , sum (case when technic_integration_id =3 then product_value else 0 end) as col_sale \n");
        sql.append("       ,count(case when technic_integration_id =4 then product_value else null end) as spa_num \n");
        sql.append("       , sum (case when technic_integration_id =4 then product_value else 0 end) as spa_sale \n");
        sql.append("       ,count(case when technic_integration_id =5 then product_value else null end) as treat_num \n");
        sql.append("       , sum (case when technic_integration_id =5 then product_value else 0 end) as treat_sale \n");
        sql.append("       , sum(product_num) as item_num \n");
        sql.append("     from  \n");
        sql.append("      (  		select dsd1.product_value,dsd1.product_id , dsd1.product_num   \n");
        sql.append("                     from data_sales ds1   \n");
        sql.append("                     inner join data_sales_detail as dsd1 on ds1.shop_id = dsd1.shop_id  and ds1.slip_no = dsd1.slip_no    \n");
        sql.append("                     where product_division = 1     \n");
        sql.append("                            and  ds1.shop_id =  ").append(SQLUtil.convertForSQL(shopID));
        sql.append("                             and date_part('year',ds1.sales_date) =   ").append(SQLUtil.convertForSQL(year));
        sql.append("                             and date_part('month',ds1.sales_date) =  ").append(SQLUtil.convertForSQL(month));
        sql.append("        )ds4 inner join \n ");
        sql.append("        (    select mt.technic_id, mtc.technic_integration_id \n");
        sql.append("             from mst_technic mt \n ");
        sql.append("             inner join mst_technic_class mtc on mt.technic_class_id= mtc.technic_class_id  \n ");
        sql.append("             )  mt1 on  ds4.product_id = mt1.technic_id \n ");

        sql.append("        )tb3, \n ");
        sql.append("        ( \n ");
        sql.append("            select count(case when ( date_part('year',now() )- date_part('year',birthday) <25) then 1 else null end ) as y_num,  \n ");
        sql.append("            count(case when ( ( date_part('year',now() )- date_part('year',birthday) >25 ) and ( date_part('year',now() )- date_part('year',birthday) <50 )) then 1 else null end ) as m_num,  \n ");
        sql.append("           count(case when ( date_part('year',now() )- date_part('year',birthday) >50) then 1 else null end ) as o_num,  \n ");
        sql.append("           count(birthday is null ) as un_num  \n ");
        sql.append("           from mst_customer  \n ");
        sql.append("          where customer_id in   \n ");
        sql.append("                 (select distinct customer_id    \n ");
        sql.append("                  from data_sales ds     \n ");
        sql.append("                   where shop_id =  ").append(SQLUtil.convertForSQL(shopID)).append(" \n ");
        sql.append("                           and date_part('year',sales_date) = ").append(SQLUtil.convertForSQL(year)).append("\n ");
        sql.append("                           and date_part('month',sales_date) = ").append(SQLUtil.convertForSQL(month)).append("\n ");
        sql.append("                   )\n ");
        sql.append("        ) tb4,\n ");
        sql.append("       (	select * \n ");
        sql.append("            from  \n ");
        sql.append("            (	select count( customer_id) as new_num  \n ");
        sql.append("                     from data_sales  \n ");
        sql.append("                     where shop_id = ").append(SQLUtil.convertForSQL(shopID)).append(" \n ");
        sql.append("                     and date_part('year',sales_date) = ").append(SQLUtil.convertForSQL(year)).append("  \n ");
        sql.append("                     and date_part('month',sales_date) = ").append(SQLUtil.convertForSQL(month)).append("  \n ");
        sql.append("                     and visit_num >1 \n ");
        sql.append("              ) ds1, \n ");
        sql.append("             (	select count (  distinct customer_id ) as usally_num  \n ");
        sql.append("                   from data_sales  \n ");
        sql.append("                   where shop_id = ").append(SQLUtil.convertForSQL(shopID)).append(" \n ");
        sql.append("                   and sales_date < date_trunc('MONTH', to_date('").append(SQLUtil.convertForSQL(year)).append(SQLUtil.convertForSQL(month)).append("01','YYYYMMDD')) \n ");
        sql.append("                   and sales_date > date_trunc('MONTH', to_date('").append(SQLUtil.convertForSQL(year)).append(SQLUtil.convertForSQL(month)).append("01','YYYYMMDD'))  + INTERVAL '-3 MONTH - 1 day' \n ");
        sql.append("                   and customer_id in (  \n ");
        sql.append("                                       select  distinct customer_id  \n ");
        sql.append("                                       from data_sales \n ");
        sql.append("                                       where shop_id = ").append(SQLUtil.convertForSQL(shopID)).append(" \n ");
        sql.append("                                       and date_part('year',sales_date) = ").append(SQLUtil.convertForSQL(year)).append(" \n ");
        sql.append("                                       and date_part('month',sales_date) = ").append(SQLUtil.convertForSQL(month)).append(" \n) ");
        sql.append("                ) ds2 , \n");
        sql.append("                (	select count ( distinct customer_id ) as new_repert_num \n");
        sql.append("                    from data_sales \n");
        sql.append("                    where shop_id = ").append(SQLUtil.convertForSQL(shopID)).append(" \n");
        sql.append("                    and sales_date < date_trunc('MONTH', to_date('").append(SQLUtil.convertForSQL(year)).append(SQLUtil.convertForSQL(month)).append("01','YYYYMMDD'))  + INTERVAL '-3 MONTH - 1 day' \n");
        sql.append("                    and customer_id in ( \n");
        sql.append("                                        select  distinct customer_id \n ");
        sql.append("                                        from data_sales  \n");
        sql.append("                                        where shop_id = ").append(SQLUtil.convertForSQL(shopID)).append("  \n");
        sql.append("                                       and date_part('year',sales_date) = ").append(SQLUtil.convertForSQL(year)).append(" \n ");
        sql.append("                                       and date_part('month',sales_date) = ").append(SQLUtil.convertForSQL(month)).append(" \n) ");
        sql.append("                                      and customer_id not in (select customer_id  ");
        sql.append("                                                                from data_sales \n ");
        sql.append("                                                                where shop_id = ").append(SQLUtil.convertForSQL(shopID)).append(" \n ");
        sql.append("                                                                and date_part('year',sales_date) = ").append(SQLUtil.convertForSQL(year)).append(" \n ");
        sql.append("                                                                and date_part('month',sales_date) = ").append(SQLUtil.convertForSQL(month)).append(" \n) ");
        sql.append("                                          ");
        sql.append("                       ) ds3");
        sql.append("                       )tb5,(");
        sql.append("                             select count(customer_id )as intro_num");
        sql.append("                             from mst_customer");
        sql.append("                            where customer_id in (");
        sql.append("                                    select  distinct customer_id");
        sql.append("                                    from data_sales");
        sql.append("                                    where shop_id = ").append(SQLUtil.convertForSQL(shopID)).append(" \n ");
        sql.append("                                      and date_part('year',sales_date) = ").append(SQLUtil.convertForSQL(year)).append(" \n ");
        sql.append("                                      and date_part('month',sales_date) = ").append(SQLUtil.convertForSQL(month)).append(" \n) ");
        sql.append("                          and introducer_id >0");
        sql.append("                       ) tb6");


        return sql.toString();
    }

    public boolean saveToDatabase(){
        if( this.size() < MONTHS_COUNT ){
            return false;
        }
        try{
            ConnectionWrapper dbAccess = SystemInfo.getConnection();
            DataTargetOX itemTargetOX = new DataTargetOX();
            dbAccess.begin();
            for( int index=0; index < MONTHS_COUNT; index++ ){
                itemTargetOX = new DataTargetOX();
                itemTargetOX = this.get( index );
                if(itemTargetOX.isExists(dbAccess)){
                    if(!itemTargetOX.UpdatetData(dbAccess)){
                        dbAccess.rollback();
                        return false;
                    }
                }else{
                    if(!itemTargetOX.insertData(dbAccess)){
                        dbAccess.rollback();
                        return false;
                    }
                }
            }
            dbAccess.commit();
            dbAccess.close();
            return true;
        }catch(Exception e){
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
    }

    /**
     * スタッフ区分マスタデータをArrayListに読み込む。
     */
    public void loadAllTargetDataOX(ConnectionWrapper con,Integer shopId,int year) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(getSelectAllSQL(shopId,year));
        int month = 0;
        int index = 1;
        DataTargetOX msc = new DataTargetOX();
        while (rs.next()) {
            month = rs.getInt("month");
            while(index < month && index <= 12){
                msc = new DataTargetOX();
                msc.clear();
                this.add(msc);
                index++;
            }
            msc = new DataTargetOX();
            msc.clear();
            msc.setDataOX(rs);
            this.add(msc);
            index++;
        }
        while(index <= 12){
            msc = new DataTargetOX();
            msc.clear();
            this.add(msc);
            index++;
        }
        rs.close();
    }

    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectAllSQL(Integer shopId,int year) {
        return " select * \n"
                + "from data_target_ox \n"
                + "where shop_id =  " + SQLUtil.convertForSQL(shopId) + "\n"
                + "      and year = " + SQLUtil.convertForSQL(year) + "\n"
                +" order by month ";

    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.basicinfo.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.HashMap;

/**
 *
 * @author nakhoa
 */
public class ListDataTargetDayOX {

    private HashMap a = new HashMap();
    private int targetDay = 0;
    private int targetYear = 0;
    private int targetMonth = 0;
    private Integer dayOfMonth = 28;
    private Integer shopID = null;
    // Product
    private HashMap hashTechnic = new HashMap();
    private HashMap hashItem = new HashMap();
    private HashMap hashNominat = new HashMap();
    // Cota
    private HashMap hashCota = new HashMap();
    // Data Sales Detail
    private HashMap hashCutNum = new HashMap();
    private HashMap hashCutSales = new HashMap();
    private HashMap hashPermNum = new HashMap();
    private HashMap hashPermSales = new HashMap();
    private HashMap hashColNum = new HashMap();
    private HashMap hashColSales = new HashMap();
    private HashMap hashSpaNum = new HashMap();
    private HashMap hashSpaSales = new HashMap();
    private HashMap hashTreatNum = new HashMap();
    private HashMap hashTreatSales = new HashMap();
    private HashMap hashItemNum = new HashMap();
    // Customer Sales
    private HashMap hashYNum = new HashMap();
    private HashMap hashMNum = new HashMap();
    private HashMap hashONum = new HashMap();
    private HashMap hashUNNum = new HashMap();
    // New Num
    private HashMap hashNewNum = new HashMap();
    // UsallyNum
    private HashMap hashUsallyNum = new HashMap();
    // NewRepertNum
    private HashMap hashNewRepertNum = new HashMap();
    private HashMap hashIntroNum = new HashMap();

    public void setDayOfMonth(Integer day){
        this.dayOfMonth = day;
    }

    public Integer getDayOfMonth(){
        return this.dayOfMonth;
    }

    public HashMap getHashTechnic() {
        return this.hashTechnic;
    }

    public HashMap getHashItem() {
        return this.hashItem;
    }

    public HashMap getHashNominat() {
        return this.hashNominat;
    }

    public HashMap getHashCota() {
        return this.hashCota;
    }

    public HashMap getHashCutNum() {
        return this.hashCutNum;
    }

    public HashMap getHashCutSales() {
        return this.hashCutSales;
    }

    public HashMap getHashPermNum() {
        return this.hashPermNum;
    }

    public HashMap getHashPermSales() {
        return this.hashPermNum;
    }

    public HashMap getHashColNum() {
        return this.hashColNum;
    }

    public HashMap getHashColSales() {
        return this.hashColSales;
    }

    public HashMap getHashSpaNum() {
        return this.hashSpaNum;
    }

    public HashMap getHashSpaSales() {
        return this.hashSpaSales;
    }

    public HashMap getHashTreatNum() {
        return this.hashTreatNum;
    }

    public HashMap getHashTreatSales() {
        return this.hashTreatSales;
    }

    public HashMap getHashItemNum() {
        return this.hashItemNum;
    }

    public HashMap getHashIntroNum() {
        return this.hashIntroNum;
    }

    public HashMap getHashYNum(){
        return this.hashYNum;
    }

    public HashMap getHashMNum(){
        return this.hashMNum;
    }

    public HashMap getHashONum(){
        return this.hashONum;
    }

    public HashMap getHashUnNum(){
        return this.hashUNNum;
    }

    public HashMap getHashNewNum() {
        return this.hashNewNum;
    }

    public HashMap getHashUsallyNum() {
        return this.hashUsallyNum;
    }

    public HashMap getHashNewRepertNum() {
        return this.hashNewRepertNum;
    }

    public void setTargetDay(int targetDay) {
        this.targetDay = targetDay;
    }

    public int getTargetDay() {
        return this.targetDay;
    }

    public void setTargetYear(int targetYear) {
        this.targetYear = targetYear;
    }

    public int getTargetYear() {
        return this.targetYear;
    }

    public void setTargetMonth(int targetMonth) {
        this.targetMonth = targetMonth;
    }

    public int getTargetMonth() {
        return this.targetMonth;
    }

    public void setShopId(Integer shopId) {
        this.shopID = shopId;
    }

    public Integer getShopId() {
        return this.shopID;
    }

    public void resetHash() {
        this.hashTechnic = new HashMap();
        this.hashItem = new HashMap();
        this.hashNominat = new HashMap();
        this.hashCota = new HashMap();
        this.hashCutNum = new HashMap();
        this.hashCutSales = new HashMap();
        this.hashPermNum = new HashMap();
        this.hashPermSales = new HashMap();
        this.hashColNum = new HashMap();
        this.hashColSales = new HashMap();
        this.hashSpaNum = new HashMap();
        this.hashSpaSales = new HashMap();
        this.hashTreatNum = new HashMap();
        this.hashTreatSales = new HashMap();
        this.hashItemNum = new HashMap();
        this.hashYNum = new HashMap();
        this.hashMNum = new HashMap();
        this.hashONum = new HashMap();
        this.hashUNNum = new HashMap();
        this.hashNewNum = new HashMap();
        this.hashUsallyNum = new HashMap();
        this.hashNewRepertNum = new HashMap();
        this.hashIntroNum = new HashMap();
    }

    public ArrayList<DataTargetDayOX> loadAllTargetDataDayOX(ConnectionWrapper con) throws SQLException {
        ArrayList<DataTargetDayOX> listItemTargetDayOX = new ArrayList<DataTargetDayOX>();
        ResultSetWrapper rs = con.executeQuery(getSelectAllSQL());
        int salesDay = 0;
        int index = 1;
        DataTargetDayOX msc = new DataTargetDayOX();
        while (rs.next()) {
            salesDay = rs.getInt("saleday");
            while(index < salesDay && index <= this.dayOfMonth){
                msc = new DataTargetDayOX();
                msc.clear();
                listItemTargetDayOX.add(msc);
                index++;
            }
            msc = new DataTargetDayOX();
            msc.setDataDayOX(rs);
            listItemTargetDayOX.add(msc);
            index++;
        }
        while(index <= this.dayOfMonth){
            msc = new DataTargetDayOX();
            msc.clear();
            listItemTargetDayOX.add(msc);
            index++;
        }
        rs.close();
        return listItemTargetDayOX;
    }

    public boolean saveToDatabase(ArrayList<DataTargetDayOX> listItemTargetDayOX){
        boolean result = true;
        if(listItemTargetDayOX.size() <= 0){
            return false;
        }
        ConnectionWrapper con = SystemInfo.getConnection();
        try{
            DataTargetDayOX itemDayOX = new DataTargetDayOX();
            con.begin();
            for(int day = 1;day <= listItemTargetDayOX.size();day++ ){
                itemDayOX = new DataTargetDayOX();
                itemDayOX = listItemTargetDayOX.get(day-1);
                if(itemDayOX.isExists(con)){
                    if(!itemDayOX.UpdatetData(con)){
                        con.rollback();
                        return false;
                    }
                }else{
                    if(!itemDayOX.insertData(con)){
                        con.rollback();
                        return false;
                    }
                }
            }
            if(result){
                con.commit();
            }else{
                con.rollback();
            }
            con.close();
            return true;
        }catch(Exception e){
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
    }

    public boolean LoadAllData() {
        boolean result = false;
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            this.resetHash();
            this.getProductData(con);
            this.getCotaData(con);
            this.getDataSalesDetailData(con);
            this.getCustomerSalesData(con);
            this.getNewNumData(con);
            this.getUsallyNumData(con);
            this.getNewRepertData(con);
            this.getIntroNumData(con);
            con.close();
            result = true;
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            result = false;
        }
        return result;
    }

    public void getProductData(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(getSelectProductSQL());
        Integer i = 1;
        Integer saleDate = 0;
        Integer technic = 0;
        Integer item = 0;
        Integer nominat = 0;
        while (rs.next()) {
            saleDate = rs.getInt("saleday");
            technic = rs.getInt("tectnic");
            item = rs.getInt("item");
            nominat = rs.getInt("nominat_num");
            while (i < saleDate && i < this.targetDay) {
                this.hashTechnic.put(i, 0);
                this.hashItem.put(i, 0);
                this.hashNominat.put(i, 0);
                i++;
            }
            this.hashTechnic.put(saleDate, technic);
            this.hashItem.put(saleDate, item);
            this.hashNominat.put(saleDate, nominat);
            i++;
        }
        while (i <= this.targetDay) {
            this.hashTechnic.put(i, 0);
            this.hashItem.put(i, 0);
            this.hashNominat.put(i, 0);
            i++;
        }
    }

    public void getCotaData(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(getSelectCotaSQL());
        Integer i = 1;
        Integer saleDate = 0;
        Integer cota = 0;
        while (rs.next()) {
            saleDate = rs.getInt("saleday");
            cota = rs.getInt("cota");
            while (i < saleDate && i < this.targetDay) {
                this.hashCota.put(i, 0);
                i++;
            }
            this.hashCota.put(saleDate, cota);
            i++;
        }
        while (i <= this.targetDay) {
            this.hashCota.put(i, 0);
            i++;
        }
    }

    public void getDataSalesDetailData(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(getSelectDataSalesDetailSQL());
        Integer i = 1;
        Integer saleDate = 0;
        Integer cutNum = 0;
        Integer cutSales = 0;
        Integer permNum = 0;
        Integer permSales = 0;
        Integer colNum = 0;
        Integer colSales = 0;
        Integer spaNum = 0;
        Integer spaSales = 0;
        Integer treatNum = 0;
        Integer treatSales = 0;
        Integer itemNum = 0;
        while (rs.next()) {
            saleDate = rs.getInt("saleday");
            cutNum = rs.getInt("cut_num");
            cutSales = rs.getInt("cut_sale");
            permNum = rs.getInt("perm_num");
            permSales = rs.getInt("perm_sale");
            colNum = rs.getInt("col_num");
            colSales = rs.getInt("col_sale");
            spaNum = rs.getInt("spa_num");
            spaSales = rs.getInt("spa_sale");
            treatNum = rs.getInt("treat_num");
            treatSales = rs.getInt("treat_sale");
            itemNum = rs.getInt("item_num");
            while (i < saleDate && i < this.targetDay) {
                this.hashCutNum.put(i, 0);
                this.hashCutSales.put(i, 0);
                this.hashPermNum.put(i, 0);
                this.hashPermSales.put(i, 0);
                this.hashColNum.put(i, 0);
                this.hashColSales.put(i, 0);
                this.hashSpaNum.put(i, 0);
                this.hashSpaSales.put(i, 0);
                this.hashTreatNum.put(i, 0);
                this.hashTreatSales.put(i, 0);
                this.hashItemNum.put(i, 0);
                i++;
            }
            this.hashCutNum.put(saleDate, cutNum);
            this.hashCutSales.put(saleDate, cutSales);
            this.hashPermNum.put(saleDate, permNum);
            this.hashPermSales.put(saleDate, permSales);
            this.hashColNum.put(saleDate, colNum);
            this.hashColSales.put(saleDate, colSales);
            this.hashSpaNum.put(saleDate, spaNum);
            this.hashSpaSales.put(saleDate, spaSales);
            this.hashTreatNum.put(saleDate, treatNum);
            this.hashTreatSales.put(saleDate, treatSales);
            this.hashItemNum.put(saleDate, itemNum);
            i++;
        }
        while (i <= this.targetDay) {
            this.hashCutNum.put(i, 0);
            this.hashCutSales.put(i, 0);
            this.hashPermNum.put(i, 0);
            this.hashPermSales.put(i, 0);
            this.hashColNum.put(i, 0);
            this.hashColSales.put(i, 0);
            this.hashSpaNum.put(i, 0);
            this.hashSpaSales.put(i, 0);
            this.hashTreatNum.put(i, 0);
            this.hashTreatSales.put(i, 0);
            this.hashItemNum.put(i, 0);
            i++;
        }
    }

    private String getSelectProductSQL() {
        return " select \n"
                + "sum(case when  product_division = 1 then product_value else 0 end ) as tectnic  \n"
                + ", sum(case when  product_division = 2 then product_value else 0 end ) as item  \n"
                + ", count(case when ds.designated_flag = true then 1 else null end ) as nominat_num \n"
                + ",ds.sales_date,extract('day' from ds.sales_date) as saleday \n"
                + "from data_sales ds \n"
                + "     inner join data_sales_detail  dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.delete_date is null \n"
                + "where ds.shop_id =  " + SQLUtil.convertForSQL(this.getShopId()) + "\n"
                + "      and date_part('year',ds.sales_date) = '" + SQLUtil.convertForSQL(this.getTargetYear()) + "'\n"
                + "      and date_part('month',ds.sales_date) = '" + SQLUtil.convertForSQL(this.getTargetMonth()) + "'\n"
                + "      and ds.delete_date is null " + "\n"
                + "group by ds.sales_date \n"
                + "order by saleday \n";
    }

    private String getSelectCotaSQL() {
        return " select sum(product_value) as cota , extract('day' from ds3.sales_date) as saleday \n"
                + "from  \n"
                + "(  	select dsd1.product_value,dsd1.product_id , ds1.sales_date \n"
                + "     from data_sales ds1 \n"
                + "         inner join data_sales_detail as dsd1 on ds1.shop_id = dsd1.shop_id \n"
                + "             and ds1.slip_no = dsd1.slip_no and dsd1.delete_date is null \n"
                + "     where product_division = 2 \n"
                + "         and ds1.shop_id =  '" + SQLUtil.convertForSQL(this.getShopId()) + "'\n"
                + "         and date_part('year',ds1.sales_date) = '" + SQLUtil.convertForSQL(this.getTargetYear()) + "'\n"
                + "         and date_part('month',ds1.sales_date) = '" + SQLUtil.convertForSQL(this.getTargetMonth()) + "'\n"
                + "         and ds1.delete_date is null \n "
                + ") ds3 inner join \n"
                + "     (   select mi.item_id \n"
                + "         from mst_item  mi \n"
                + "             inner join mst_item_class  mic on mi.item_class_id = mic.item_class_id \n"
                + "         where mic.item_integration_id = 1 " + "\n"
                + "              and mi.delete_date is null \n"
                + "      ) mi1 on mi1.item_id = ds3.product_id "
                + "group by ds3.sales_date \n"
                + "order by saleday \n";
    }

    private String getSelectDataSalesDetailSQL() {
        return " select count(case when technic_integration_id =1 then product_value else null end) as cut_num \n"
                + ", sum (case when technic_integration_id =1 then product_value else 0 end) as cut_sale  \n"
                + ",count(case when technic_integration_id =2 then product_value else null end) as perm_num  \n"
                + ", sum (case when technic_integration_id =2 then product_value else 0 end) as perm_sale  \n"
                + ",count(case when technic_integration_id =3 then product_value else null end) as col_num  \n"
                + ", sum (case when technic_integration_id =3 then product_value else 0 end) as col_sale  \n"
                + ",count(case when technic_integration_id =4 then product_value else null end) as spa_num  \n"
                + ", sum (case when technic_integration_id =4 then product_value else 0 end) as spa_sale  \n"
                + ",count(case when technic_integration_id =5 then product_value else null end) as treat_num  \n"
                + ", sum (case when technic_integration_id =5 then product_value else 0 end) as treat_sale  \n"
                + ", sum(product_num) as item_num ,extract('day' from sales_date) as saleday  \n"
                + "from  \n"
                + "     (	select dsd1.product_value,dsd1.product_id , dsd1.product_num ,ds1.sales_date \n"
                + "             from data_sales ds1 \n"
                + "                 inner join data_sales_detail as dsd1 on ds1.shop_id = dsd1.shop_id \n"
                + "                     and ds1.slip_no = dsd1.slip_no and dsd1.delete_date is null \n"
                + "             where product_division = 1  \n"
                + "                 and ds1.shop_id =  " + SQLUtil.convertForSQL(this.getShopId()) + "\n"
                + "                 and date_part('year',ds1.sales_date) = '" + SQLUtil.convertForSQL(this.getTargetYear()) + "'\n"
                + "                 and date_part('month',ds1.sales_date) = '" + SQLUtil.convertForSQL(this.getTargetMonth()) + "'\n"
                + "                 and ds1.delete_date is null \n "
                + ")ds4 inner join \n"
                + "     (    	select mt.technic_id, mtc.technic_integration_id \n"
                + "             from mst_technic mt \n"
                + "                 inner join mst_technic_class mtc on mt.technic_class_id= mtc.technic_class_id and mt.delete_date is null  \n"
                + "     )  mt1 on  ds4.product_id = mt1.technic_id \n "
                + "group by saleday \n"
                + "order by saleday \n";
    }

    public void getCustomerSalesData(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = new ResultSetWrapper();
        for (int day = 1; day <= this.targetDay; day++) {
            rs = new ResultSetWrapper();
            rs = con.executeQuery(getSelectCustomerSalesSQL(day));
            if (rs.next()) {
                this.hashYNum.put(day, rs.getInt("y_num"));
                this.hashMNum.put(day, rs.getInt("m_num"));
                this.hashONum.put(day, rs.getInt("o_num"));
                this.hashUNNum.put(day, rs.getInt("un_num"));
            } else {
                this.hashYNum.put(day, 0);
                this.hashMNum.put(day, 0);
                this.hashONum.put(day, 0);
                this.hashUNNum.put(day, 0);
            }
        }
    }

    private String getSelectCustomerSalesSQL(int targetday) {
        return " select count(case when ( date_part('year',now() )- date_part('year',birthday) <25) then 1 else null end ) as y_num \n"
                + ",count(case when ( ( date_part('year',now() )- date_part('year',birthday) >25 )  \n"
                + "    and ( date_part('year',now() )- date_part('year',birthday) <50 )) then 1 else null end ) as m_num  \n"
                + ",count(case when ( date_part('year',now() )- date_part('year',birthday) >50) then 1 else null end ) as o_num \n"
                + ",count(birthday is null ) as un_num \n"
                + "from mst_customer \n"
                + "where customer_id in \n"
                + "     (select distinct customer_id  \n"
                + "      from data_sales ds \n"
                + "      where shop_id = " + SQLUtil.convertForSQL(this.getShopId()) + "\n"
                + "         and date_part('year',sales_date) = '" + SQLUtil.convertForSQL(this.getTargetYear()) + "'\n"
                + "         and date_part('month',sales_date) = '" + SQLUtil.convertForSQL(this.getTargetMonth()) + "'\n"
                + "          and date_part('day',sales_date) = '" + SQLUtil.convertForSQL(targetday) + "'\n"
                + "         and ds.delete_date is null ) \n"
                + "     and delete_date is null ";
    }

    public void getNewNumData(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(getSelectNewNumSQL());
        Integer i = 1;
        Integer saleDate = 0;
        Integer newNum = 0;
        while (rs.next()) {
            saleDate = rs.getInt("saleday");
            newNum = rs.getInt("new_num");
            while (i < saleDate && i < this.targetDay) {
                this.hashNewNum.put(i, 0);
                i++;
            }
            this.hashNewNum.put(saleDate, newNum);
            i++;
        }
        while (i <= this.targetDay) {
            this.hashNewNum.put(i, 0);
            i++;
        }
    }

    private String getSelectNewNumSQL() {
        return " select count( customer_id) as new_num , extract('day' from sales_date) as saleday \n"
                + "from data_sales  \n"
                + "where shop_id =  '" + SQLUtil.convertForSQL(this.getShopId()) + "'\n"
                + "      and date_part('year',sales_date) = '" + SQLUtil.convertForSQL(this.getTargetYear()) + "'\n"
                + "      and date_part('month',sales_date) = '" + SQLUtil.convertForSQL(this.getTargetMonth()) + "'\n"
                + "       and visit_num >1"
                + "      and delete_date is null " + "\n"
                + "group by saleday \n"
                + "order by saleday \n";
    }

    public void getUsallyNumData(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(getSelectUsallyNumSQL());
        Integer i = 1;
        Integer saleDate = 0;
        Integer usallyNum = 0;
        while (rs.next()) {
            saleDate = rs.getInt("saleday");
            usallyNum = rs.getInt("usally_num");
            while (i < saleDate && i < this.targetDay) {
                this.hashUsallyNum.put(i, 0);
                i++;
            }
            this.hashUsallyNum.put(saleDate, usallyNum);
            i++;
        }
        while (i <= this.targetDay) {
            this.hashUsallyNum.put(i, 0);
            i++;
        }
    }

    private String getSelectUsallyNumSQL() {
        StringBuilder sql = new StringBuilder();
        sql.append("select count (  distinct customer_id ) as usally_num ,extract('day' from sales_date) as saleday \n");
        sql.append("from data_sales  \n ");
        sql.append("where shop_id = ").append(SQLUtil.convertForSQL(this.getShopId())).append(" \n ");
        sql.append(" and sales_date < date_trunc('MONTH', to_date('").append(SQLUtil.convertForSQL(this.getTargetYear()));
        sql.append(SQLUtil.convertForSQL(this.getTargetMonth())).append("01','YYYYMMDD')) \n ");
        sql.append(" and sales_date > date_trunc('MONTH', to_date('").append(SQLUtil.convertForSQL(this.getTargetYear()));
        sql.append(SQLUtil.convertForSQL(this.getTargetMonth())).append("01','YYYYMMDD'))  + INTERVAL '-3 MONTH - 1 day' \n ");
        sql.append(" and customer_id in (  \n ");
        sql.append("        select  distinct customer_id  \n ");
        sql.append("        from data_sales \n ");
        sql.append("        where shop_id = ").append(SQLUtil.convertForSQL(this.getShopId())).append(" \n ");
        sql.append("            and date_part('year',sales_date) = ").append(SQLUtil.convertForSQL(this.getTargetYear())).append(" \n ");
        sql.append("            and date_part('month',sales_date) = ").append(SQLUtil.convertForSQL(this.getTargetMonth())).append(" \n) ");
        sql.append("            and delete_date is null \n");
        sql.append("group by saleday \n");
        sql.append("order by saleday ");

        return sql.toString();
    }

    public void getNewRepertData(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(getSelectNewRepertNumSQL());
        Integer i = 1;
        Integer saleDate = 0;
        Integer newRepertNum = 0;
        while (rs.next()) {
            saleDate = rs.getInt("saleday");
            newRepertNum = rs.getInt("new_repert_num");
            while (i < saleDate && i < this.targetDay) {
                this.hashNewRepertNum.put(i, 0);
                i++;
            }
            this.hashNewRepertNum.put(saleDate, newRepertNum);
            i++;
        }
        while (i <= this.targetDay) {
            this.hashNewRepertNum.put(i, 0);
            i++;
        }
    }

    private String getSelectNewRepertNumSQL() {
        StringBuilder sql = new StringBuilder();
        sql.append("select count ( distinct customer_id ) as new_repert_num , extract('day' from sales_date) as saleday \n");
        sql.append("from data_sales \n");
        sql.append("where shop_id = ").append(SQLUtil.convertForSQL(this.getShopId())).append(" \n");
        sql.append("    and sales_date < date_trunc('MONTH', to_date('").append(SQLUtil.convertForSQL(this.getTargetYear()));
        sql.append(SQLUtil.convertForSQL(this.getTargetMonth())).append("01','YYYYMMDD'))  + INTERVAL '-3 MONTH - 1 day' \n");
        sql.append("    and customer_id in ( \n");
        sql.append("        select  distinct customer_id \n ");
        sql.append("        from data_sales  \n");
        sql.append("        where shop_id = ").append(SQLUtil.convertForSQL(this.getShopId())).append("  \n");
        sql.append("            and date_part('year',sales_date) = ").append(SQLUtil.convertForSQL(this.getTargetYear())).append(" \n ");
        sql.append("            and date_part('month',sales_date) = ").append(SQLUtil.convertForSQL(this.getTargetMonth())).append(" \n) ");
        sql.append("            and customer_id not in (select customer_id  ");
        sql.append("                from data_sales \n ");
        sql.append("                where shop_id = ").append(SQLUtil.convertForSQL(this.getShopId())).append(" \n ");
        sql.append("                    and date_part('year',sales_date) = ").append(SQLUtil.convertForSQL(this.getTargetYear())).append(" \n ");
        sql.append("                    and date_part('month',sales_date) = ").append(SQLUtil.convertForSQL(this.getTargetMonth())).append(" \n) ");
        sql.append("    and delete_date is null \n");
        sql.append("group by saleday \n");
        sql.append("order by saleday ");
        return sql.toString();
    }

    public void getIntroNumData(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = new ResultSetWrapper();
        for (int day = 1; day <= this.targetDay; day++) {
            rs = new ResultSetWrapper();
            rs = con.executeQuery(getSelectIntroNumSQL(day));
            if (rs.next()) {
                this.hashIntroNum.put(day, rs.getInt("intro_num"));
            } else {
                this.hashIntroNum.put(day, 0);
            }
        }
    }

    private String getSelectIntroNumSQL(int targetday) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(customer_id )as intro_num \n");
        sql.append("from mst_customer \n");
        sql.append("where customer_id in ( \n");
        sql.append("        select  distinct customer_id \n");
        sql.append("        from data_sales \n ");
        sql.append("        where shop_id = ").append(SQLUtil.convertForSQL(this.getShopId())).append(" \n ");
        sql.append("            and date_part('year',sales_date) = ").append(SQLUtil.convertForSQL(this.getTargetYear())).append(" \n ");
        sql.append("            and date_part('month',sales_date) = ").append(SQLUtil.convertForSQL(this.getTargetMonth())).append(" \n ");
        sql.append("            and date_part('day',sales_date) = ").append(SQLUtil.convertForSQL(targetday)).append(" \n ");
        sql.append("            and delete_date is null ) \n");
        sql.append("    and delete_date is null \n");
        sql.append("    and introducer_id >0");
        return sql.toString();
    }

    /**
     * Selectï∂ÇéÊìæÇ∑ÇÈÅB
     *
     * @return Selectï∂
     */
    private String getSelectAllSQL() {
        return " select * , EXTRACT('day' from day) as saleday \n"
                + "from data_target_day_ox \n"
                + "where shop_id =  " + SQLUtil.convertForSQL(this.getShopId()) + "\n"
                + "      and EXTRACT('year' from day) = " + SQLUtil.convertForSQL(this.getTargetYear()) + "\n"
                + "      and EXTRACT('month' from day) = "+ SQLUtil.convertForSQL(this.getTargetMonth()) + "\n"
                +"order by saleday \n";

    }
}

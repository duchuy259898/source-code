/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report;

import static com.cortexeb.tools.clover.L.s;
import com.geobeck.sosia.pos.master.company.MstShopSetting;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.swing.MessageDialog;
import com.geobeck.util.SQLUtil;
import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author nahoang
 */
public class MasherRanking5List extends ArrayList<CustomerRanking> {
    private final int RANK_TECHNIC = 1;
    private final int RANK_ITEM_USE = 2;
    private final int TAX_TYPE_BLANK = 1;	//税抜
    //IVS_LVTu start add 2015/05/21 New request #36880
    private Integer shopId      = null;

    private Integer staffId      = null;
    
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }
    
    //IVS_LVTu end add 2015/05/21 New request #36880
   
    /**
     * Check customerId to be exist in database
     * @param customerId
     * @param categoryId
     * @return true or false
     */
    public boolean isExistCustomer(int customerId, int categoryId) {
        String sqlQuery = "";
        sqlQuery = "select customer_id from mst_customer_rank where customer_id= " + customerId+
                 " and shop_category_id =" +categoryId;
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(sqlQuery);
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
            return false;
        }
        return false;
    }

    /**
     * Insert record into database 
     * @return true or false
     */
    public boolean insertCustomerRanking(int customerId, int categoryId, Long technicRankId, Long itemRankId) throws SQLException {
        String sqlQuery = "";
        sqlQuery = "insert into mst_customer_rank ("
                + "customer_id, "
                + "shop_category_id, "
                + "technic_rank_id, "
                + "item_rank_id, "
                + "insert_date, "
                + "update_date ) "
                + "values("
                + SQLUtil.convertForSQL(customerId) + ","
                + SQLUtil.convertForSQL(categoryId) + ","
                + SQLUtil.convertForSQL(technicRankId) + ","
                + SQLUtil.convertForSQL(itemRankId) + ","
                + " current_timestamp, current_timestamp )";
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            con.execute(sqlQuery);
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Update record in database if it is exist
     * @param typeOfRank
     * @param categoryId
     * @return true or false
     */
    public boolean updateCustomerRanking(int customerId, int categoryId, long itemId) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append(" update mst_customer_rank ");
        sqlQuery.append("   SET");
        sqlQuery.append("   item_rank_id = " + SQLUtil.convertForSQL(itemId) + "");
        sqlQuery.append("   , update_date  = current_timestamp ");
        sqlQuery.append(" where customer_id = " + SQLUtil.convertForSQL(customerId) + "");
        sqlQuery.append(" AND shop_category_id = " + SQLUtil.convertForSQL(categoryId));
        
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            con.execute(sqlQuery.toString());
        } catch (Exception e) {           
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Get list group name ranking
     * @param shopCategoryId 
     */
    public void getGroupNameRanking(int shopCategoryId) {

        ConnectionWrapper con = SystemInfo.getConnection();
        StringBuilder sqlQuery = new StringBuilder();
        //IVS_LVTu start edit 2015/02/27 new request ( MASHU様独自ランク設計書_20150224)
        String itemGroupID = "";
        //sqlQuery.append(" select r.item_group_id, g.item_group_name ");
        //sqlQuery.append(" from mst_rank_advanced_setting r inner join mst_rank_item_group g");
        //sqlQuery.append(" on r.item_group_id = g.item_group_id");
        sqlQuery.append(" select r.item_group_id ");
        sqlQuery.append(" from mst_rank_advanced_setting r");
        sqlQuery.append(" where r.shop_category_id = " + shopCategoryId + " ");

        try {
            ResultSetWrapper rs = null;
            rs = con.executeQuery(sqlQuery.toString());
            if (rs.next()) {
                //this.setItemGroupName(rs.getString("item_group_name"));
                itemGroupID = rs.getString("item_group_id");
            }else{
                this.setItemGroupName("");
                return;
            }
        } catch (Exception e) {
        }
        try {
            ResultSetWrapper rs = null;
            sqlQuery = new StringBuilder();
            sqlQuery.append( " select item_group_name from  mst_rank_item_group where item_group_id in ( " + itemGroupID + " ) order by item_group_id " );
            rs = con.executeQuery(sqlQuery.toString());
            String arrItemID = "";
            while (rs.next()) {
                arrItemID += ",<br>" + rs.getString("item_group_name");
            }
            arrItemID = arrItemID.replaceFirst(",<br>", "");
            if(!"".equals(arrItemID))
            {
                this.setItemGroupName("<html>" + arrItemID +"</html>");
            }
        } catch (Exception e) {
        }
        //IVS_LVTu end edit 2015/02/27 new request ( MASHU様独自ランク設計書_20150224)
    }

    /**
     * Get sale group name ranking
     * @param shopCategoryId
     * @return list name ranking
     */
    public ArrayList<String> getSaleNameRanking(int shopCategoryId) {
        ConnectionWrapper con = SystemInfo.getConnection();
        String itemSaleGroup = null;
        try {
            ResultSetWrapper rs = null;
            String sql = "SELECT item_sales_group FROM mst_rank_advanced_setting WHERE shop_category_id = " + shopCategoryId;
            rs = con.executeQuery(sql.toString());
            if (rs.next()) {
                itemSaleGroup = rs.getString("item_sales_group");
            }
        } catch (Exception e) {
        }
        ArrayList<String> listGroup = new ArrayList<String>();
        try {
            ResultSetWrapper rs = null;
            String sqlQuery = "";
            sqlQuery = " select item_group_name from  mst_rank_item_group where item_group_id in (" + itemSaleGroup + ")";
            rs = con.executeQuery(sqlQuery.toString());

            while (rs.next()) {
                listGroup.add(rs.getString("item_group_name"));
            }
        } catch (Exception e) {
        }

        return listGroup;
    }

    /**
     * Load item ranking with technic and item(buy or not buy) into table.
     * @param typeOfRank (technic or item)
     */
    public void loadMasherRanking(int typeOfRank) {
        //IVS_LVTu start edit 2015/02/27 new request ( MASHU様独自ランク設計書_20150224)
        try {
            
            listItemGroupId = getItemGroupID(this.getShopCagegory());
            
            if (typeOfRank == RANK_TECHNIC) {
                this.createTempTargetList(this.getShopCagegory(),typeOfRank);
                this.getMasherRankingListValue(typeOfRank);
            } else if (typeOfRank == RANK_ITEM_USE) {
                if (listItemGroupId.size() > 1) {
                    this.createTempTargetList2(this.getShopCagegory(), typeOfRank, listItemGroupId);
                }
                this.createTempTargetList(this.getShopCagegory(), typeOfRank);
                this.getMasherRankingListValue(typeOfRank);
                
            } else {
                this.createTempTargetList(this.getShopCagegory(), typeOfRank);
                this.getMasherRankingListValue(typeOfRank);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(MasherRanking5List.class.getName()).log(Level.SEVERE, null, ex);
        }
        //IVS_LVTu end edit 2015/02/27 new request ( MASHU様独自ランク設計書_20150224)
    }
    
    /**
     * Load header ranking with technic and item(buy or not buy) into table.
     * @param categoryId 
     */
    public boolean loadDataHeader(int categoryId){
        boolean isExist = this.getMasherHeaderName(categoryId);
        try {
            this.getMasherRankingName(categoryId);
        } catch (Exception ex) {
            Logger.getLogger(MasherRanking5List.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isExist;
    }
    
    /**
     * Load group name ranking with technic and item(buy or not buy) into table.
     * @param categoryId
     * @return list group name
     */
    public ArrayList<String> loadDataGroupName(int categoryId){
        ArrayList<String> listSaleGroup = new ArrayList<String>();
        this.getGroupNameRanking(categoryId);
        listSaleGroup = this.getSaleNameRanking(categoryId);
        
        return listSaleGroup;
    }

    /**
     * Get detail content of Item on table.
     *
     * @param shopCategoryId
     * @throws Exception
     */
    public void getMasherRankingName(int shopCategoryId) throws Exception {
        boolean result = true;
        String sql = "";
        sql = "select rank_id, rank_name, color from   mst_rank";
        ArrayList<MstRank> listName = new ArrayList<MstRank>();

        ResultSetWrapper rs = null;
        ConnectionWrapper con = SystemInfo.getConnection();
        rs = con.executeQuery(sql);
        while (rs.next()) {
            MstRank rankItem = new MstRank();
            rankItem.setRankId(rs.getInt("rank_id"));
            rankItem.setRankName(rs.getString("rank_name"));
            //IVS_LVTu start edit 2015/01/08 Task #34581
            String strColorCode = rs.getString("color");
            if(!"".equals(strColorCode))
            {
                rankItem.setColor(strColorCode);
            }
            else
            {
                rankItem.setColor("FFFFFF");
            }
            //IVS_LVTu end edit 2015/01/08 Task #34581
            listName.add(rankItem);
        }

        sql = "";
        sql = "select * from mst_rank_setting   where shop_category_id = " + shopCategoryId;

        rs = SystemInfo.getConnection().executeQuery(sql);

        //count nubmer column in table.
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        if (rs.next()) {

            for (int i = 1; i < columnsNumber; i++) {
                for (MstRank item : listName) {
                    if (rs.getObject(i) instanceof Integer) {
                        if (rs.getObject(i).equals(item.getRankId())) {
                            switch (i) {
                                case 2:
                                    this.setRankR3C2(item.getRankName().toString());
                                    this.setRankR3C2Value(item.getRankId());
                                    this.setRankR3C2Color(item.getColor());
                                    break;
                                case 3:
                                    this.setRankR4C2(item.getRankName().toString());
                                    this.setRankR4C2Value(item.getRankId());
                                    this.setRankR4C2Color(item.getColor());
                                    break;
                                case 4:
                                    this.setRankR5C2(item.getRankName().toString());
                                    this.setRankR5C2Value(item.getRankId());
                                    this.setRankR5C2Color(item.getColor());
                                    break;
                                case 5:
                                    this.setRankR6C2(item.getRankName().toString());
                                    this.setRankR6C2Value(item.getRankId());
                                    this.setRankR6C2Color(item.getColor());
                                    break;
                                case 6:
                                    this.setRankR7C2(item.getRankName().toString());
                                    this.setRankR7C2Value(item.getRankId());
                                    this.setRankR7C2Color(item.getColor());
                                    break;
                                case 7:
                                    this.setRankR3C3(item.getRankName().toString());
                                    this.setRankR3C3Value(item.getRankId());
                                    this.setRankR3C3Color(item.getColor());
                                    break;
                                case 8:
                                    this.setRankR4C3(item.getRankName().toString());
                                    this.setRankR4C3Value(item.getRankId());
                                    this.setRankR4C3Color(item.getColor());
                                    break;
                                case 9:
                                    this.setRankR5C3(item.getRankName().toString());
                                    this.setRankR5C3Value(item.getRankId());
                                    this.setRankR5C3Color(item.getColor());
                                    break;
                                case 10:
                                    this.setRankR6C3(item.getRankName().toString());
                                    this.setRankR6C3Value(item.getRankId());
                                    this.setRankR6C3Color(item.getColor());
                                    break;
                                case 11:
                                    this.setRankR7C3(item.getRankName().toString());
                                    this.setRankR7C3Value(item.getRankId());
                                    this.setRankR7C3Color(item.getColor());
                                    break;
                                case 12:
                                    this.setRankR3C4(item.getRankName().toString());
                                    this.setRankR3C4Value(item.getRankId());
                                    this.setRankR3C4Color(item.getColor());
                                    break;
                                case 13:
                                    this.setRankR4C4(item.getRankName().toString());
                                    this.setRankR4C4Value(item.getRankId());
                                    this.setRankR4C4Color(item.getColor());
                                    break;
                                case 14:
                                    this.setRankR5C4(item.getRankName().toString());
                                    this.setRankR5C4Value(item.getRankId());
                                    this.setRankR5C4Color(item.getColor());
                                    break;
                                case 15:
                                    this.setRankR6C4(item.getRankName().toString());
                                    this.setRankR6C4Value(item.getRankId());
                                    this.setRankR6C4Color(item.getColor());
                                    break;
                                case 16:
                                    this.setRankR7C4(item.getRankName().toString());
                                    this.setRankR7C4Value(item.getRankId());
                                    this.setRankR7C4Color(item.getColor());
                                    break;
                                case 17:
                                    this.setRankR3C5(item.getRankName().toString());
                                    this.setRankR3C5Value(item.getRankId());
                                    this.setRankR3C5Color(item.getColor());
                                    break;
                                case 18:
                                    this.setRankR4C5(item.getRankName().toString());
                                    this.setRankR4C5Value(item.getRankId());
                                    this.setRankR4C5Color(item.getColor());
                                    break;
                                case 19:
                                    this.setRankR5C5(item.getRankName().toString());
                                    this.setRankR5C5Value(item.getRankId());
                                    this.setRankR5C5Color(item.getColor());
                                    break;
                                case 20:
                                    this.setRankR6C5(item.getRankName().toString());
                                    this.setRankR6C5Value(item.getRankId());
                                    this.setRankR6C5Color(item.getColor());
                                    break;
                                case 21:
                                    this.setRankR7C5(item.getRankName().toString());
                                    this.setRankR7C5Value(item.getRankId());
                                    this.setRankR7C5Color(item.getColor());
                                    break;
                                case 22:
                                    this.setRankR3C6(item.getRankName().toString());
                                    this.setRankR3C6Value(item.getRankId());
                                    this.setRankR3C6Color(item.getColor());
                                    break;
                                case 23:
                                    this.setRankR4C6(item.getRankName().toString());
                                    this.setRankR4C6Value(item.getRankId());
                                    this.setRankR4C6Color(item.getColor());
                                    break;
                                case 24:
                                    this.setRankR5C6(item.getRankName().toString());
                                    this.setRankR5C6Value(item.getRankId());
                                    this.setRankR5C6Color(item.getColor());
                                    break;
                                case 25:
                                    this.setRankR6C6(item.getRankName().toString());
                                    this.setRankR6C6Value(item.getRankId());
                                    this.setRankR6C6Color(item.getColor());
                                    break;
                                case 26:
                                    this.setRankR7C6(item.getRankName().toString());
                                    this.setRankR7C6Value(item.getRankId());
                                    this.setRankR7C6Color(item.getColor());
                                    break;
                                case 27:
                                    this.setRankR9C2(item.getRankName().toString());
                                    this.setRankR9C2Value(item.getRankId());
                                    this.setRankR9C2Color(item.getColor());
                                    break;
                                case 28:
                                    this.setRankR10C2(item.getRankName().toString());
                                    this.setRankR10C2Value(item.getRankId());
                                    this.setRankR10C2Color(item.getColor());
                                    break;
                                case 29:
                                    this.setRankR9C3(item.getRankName().toString());
                                    this.setRankR9C3Value(item.getRankId());
                                    this.setRankR9C3Color(item.getColor());
                                    break;
                                case 30:
                                    this.setRankR10C3(item.getRankName().toString());
                                    this.setRankR10C3Value(item.getRankId());
                                    this.setRankR10C3Color(item.getColor());
                                    break;
                                case 31:
                                    this.setRankR9C4(item.getRankName().toString());
                                    this.setRankR9C4Value(item.getRankId());
                                    this.setRankR9C4Color(item.getColor());
                                    break;
                                case 32:
                                    this.setRankR10C4(item.getRankName().toString());
                                    this.setRankR10C4Value(item.getRankId());
                                    this.setRankR10C4Color(item.getColor());
                                    break;
                                case 33:
                                    this.setRankR9C5(item.getRankName().toString());
                                    this.setRankR9C5Value(item.getRankId());
                                    this.setRankR9C5Color(item.getColor());
                                    break;
                                case 34:
                                    this.setRankR10C5(item.getRankName().toString());
                                    this.setRankR10C5Value(item.getRankId());
                                    this.setRankR10C5Color(item.getColor());
                                    break;
                                case 35:
                                    this.setRankR9C6(item.getRankName().toString());
                                    this.setRankR9C6Value(item.getRankId());
                                    this.setRankR9C6Color(item.getColor());
                                    break;
                                case 36:
                                    this.setRankR10C6(item.getRankName().toString());
                                    this.setRankR10C6Value(item.getRankId());
                                    this.setRankR10C6Color(item.getColor());
                                    break;
                            }

                        }
                    }
                }
            }
        }
    }

    /**
     * Get header of table.
     *
     * @param shopCategoryId
     */
    public boolean getMasherHeaderName(int shopCategoryId) {
        boolean isExist = false;
        String sql = "";
        sql = "select * from mst_rank_advanced_setting where shop_category_id = " + shopCategoryId;

        ResultSetWrapper rs;
        try {
            rs = SystemInfo.getConnection().executeQuery(sql);
            if (rs.next()) {
                //unit price
                this.setRankU5(rs.getLong("unit_price5"));
                this.setRankU4(rs.getLong("unit_price4"));
                this.setRankU3(rs.getLong("unit_price3"));
                this.setRankU2(rs.getLong("unit_price2"));
                this.setRankU1(rs.getLong("unit_price1"));

                //visit cycle.
                this.setRankF5(rs.getLong("visit_cycle5"));
                this.setRankF4(rs.getLong("visit_cycle4"));
                this.setRankF3(rs.getLong("visit_cycle3"));
                this.setRankF2(rs.getLong("visit_cycle2"));
                this.setRankF1(rs.getLong("visit_cycle1"));

                //technic value.
                this.setRankM5(rs.getLong("technic_value5"));
                this.setRankM4(rs.getLong("technic_value4"));
                this.setRankM3(rs.getLong("technic_value3"));
                this.setRankM2(rs.getLong("technic_value2"));
                this.setRankM1(rs.getLong("technic_value1"));

                //item value
                this.setRankItemU5(rs.getLong("item_value5"));
                this.setRankItemU4(rs.getLong("item_value4"));
                this.setRankItemU3(rs.getLong("item_value3"));
                this.setRankItemU2(rs.getLong("item_value2"));
                this.setRankItemU1(rs.getLong("item_value1"));
                isExist = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MasherRanking5List.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isExist;
    }

    /**
     * Count item ranking from temp table.
     * @param typeOfRank
     * @throws Exception 
     */
    private void getMasherRankingListValue(int typeOfRank) throws Exception {
        this.clear();

        ConnectionWrapper con = SystemInfo.getConnection();
        ResultSetWrapper rs = null;
        //IVS_LVTu start add 2015/05/21 New request #36880
        String itemSaleGroup = null;
        String itemGroupId = null;
        try {
            String sql = "SELECT item_sales_group, item_group_id FROM mst_rank_advanced_setting WHERE shop_category_id = " + this.getShopCagegory();
            rs = con.executeQuery(sql.toString());
            if (rs.next()) {
                itemSaleGroup = rs.getString("item_sales_group");
                itemGroupId = rs.getString("item_group_id");
            }
        } catch (Exception e) {
         }
        
        MstRankAdvancedSetting mstRankAdS = new MstRankAdvancedSetting();
        if (typeOfRank == 1) {
            mstRankAdS.setShopCategoryId(this.getShopCagegory());
            mstRankAdS.loadRankAdvancedSettingData();
        }
        String salesValueColumn = "";
        if (getTaxType() == TAX_TYPE_BLANK) {
            //割引後税抜き金額
            salesValueColumn = "discount_detail_value_no_tax";
        } else {
            //割引後税込み金額
            salesValueColumn = "discount_detail_value_in_tax";
        }
        //IVS_LVTu end add 2015/05/21 New request #36880
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        if (typeOfRank == RANK_TECHNIC) {
            sql.append("      sum(case when date_num <= " + getRankF5() + " and sales_value >= " + getRankM5() + " then 1 else 0 end) as f5m5");
            sql.append("     ,sum(case when date_num <= " + getRankF5() + " and sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " then 1 else 0 end) as f5m4");
            sql.append("     ,sum(case when date_num <= " + getRankF5() + " and sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " then 1 else 0 end) as f5m3");
            sql.append("     ,sum(case when date_num <= " + getRankF5() + " and sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " then 1 else 0 end) as f5m2");
            sql.append("     ,sum(case when date_num <= " + getRankF5() + " and sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " then 1 else 0 end) as f5m1");

            sql.append("     ,sum(case when date_num between " + (getRankF5() + 1) + " and " + getRankF4() + " and sales_value >= " + getRankM5() + " then 1 else 0 end) as f4m5");
            sql.append("     ,sum(case when date_num between " + (getRankF5() + 1) + " and " + getRankF4() + " and sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " then 1 else 0 end) as f4m4");
            sql.append("     ,sum(case when date_num between " + (getRankF5() + 1) + " and " + getRankF4() + " and sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " then 1 else 0 end) as f4m3");
            sql.append("     ,sum(case when date_num between " + (getRankF5() + 1) + " and " + getRankF4() + " and sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " then 1 else 0 end) as f4m2");
            sql.append("     ,sum(case when date_num between " + (getRankF5() + 1) + " and " + getRankF4() + " and sales_value between " + getRankM1() + " and " + (getRankM2() - 1) +" then 1 else 0 end) as f4m1");

            sql.append("     ,sum(case when date_num between " + (getRankF4() + 1) + " and " + getRankF3() + " and sales_value >= " + getRankM5() + " then 1 else 0 end) as f3m5");
            sql.append("     ,sum(case when date_num between " + (getRankF4() + 1) + " and " + getRankF3() + " and sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " then 1 else 0 end) as f3m4");
            sql.append("     ,sum(case when date_num between " + (getRankF4() + 1) + " and " + getRankF3() + " and sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " then 1 else 0 end) as f3m3");
            sql.append("     ,sum(case when date_num between " + (getRankF4() + 1) + " and " + getRankF3() + " and sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " then 1 else 0 end) as f3m2");
            sql.append("     ,sum(case when date_num between " + (getRankF4() + 1) + " and " + getRankF3() + " and sales_value between " + getRankM1() + " and " + (getRankM2() - 1) +" then 1 else 0 end) as f3m1");

            sql.append("     ,sum(case when date_num between " + (getRankF3() + 1) + " and " + getRankF2() + " and sales_value >= " + getRankM5() + " then 1 else 0 end) as f2m5");
            sql.append("     ,sum(case when date_num between " + (getRankF3() + 1) + " and " + getRankF2() + " and sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " then 1 else 0 end) as f2m4");
            sql.append("     ,sum(case when date_num between " + (getRankF3() + 1) + " and " + getRankF2() + " and sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " then 1 else 0 end) as f2m3");
            sql.append("     ,sum(case when date_num between " + (getRankF3() + 1) + " and " + getRankF2() + " and sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " then 1 else 0 end) as f2m2");
            sql.append("     ,sum(case when date_num between " + (getRankF3() + 1) + " and " + getRankF2() + " and sales_value between " + getRankM1() + " and " + (getRankM2() - 1) +" then 1 else 0 end) as f2m1");

            sql.append("     ,sum(case when date_num between " + getRankF1() + " and " + (getCycleDate() - 1) + " and sales_value >= " + getRankM5() + " then 1 else 0 end) as f1m5");
            sql.append("     ,sum(case when date_num between " + getRankF1() + " and " + (getCycleDate() - 1) + "  and sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " then 1 else 0 end) as f1m4");
            sql.append("     ,sum(case when date_num between " + getRankF1() + " and " + (getCycleDate() - 1) + "   and sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " then 1 else 0 end) as f1m3");
            sql.append("     ,sum(case when date_num between " + getRankF1() + " and " + (getCycleDate() - 1) + "  and sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " then 1 else 0 end) as f1m2");
            sql.append("     ,sum(case when date_num between " + getRankF1() + " and " + (getCycleDate() - 1) + "   and sales_value between " + getRankM1() + " and " + (getRankM2() - 1) +" then 1 else 0 end) as f1m1");
        } else if (typeOfRank == RANK_ITEM_USE) {
            sql.append("      sum(case when sales_value >= " + getRankItemU5() + " then 1 else 0 end) as i5m5");
            sql.append("      ,sum(case when sales_value between " + getRankItemU4() + " and " + (getRankItemU5() - 1) + "then 1 else 0 end ) as i5m4");
            sql.append("      ,sum(case when sales_value between " + getRankItemU3() + " and " + (getRankItemU4() - 1) + "then 1 else 0 end) as i5m3");
            sql.append("      ,sum(case when sales_value between " + getRankItemU2() + " and " + (getRankItemU3() - 1) + "then 1 else 0 end ) as i5m2");
            sql.append("      ,sum(case when sales_value between " + getRankItemU1() + " and " + (getRankItemU2()- 1) + "then 1 else 0 end ) as i5m1");
        } else {
            sql.append("      sum( case when sales_value >= " + getRankItemU5() + " then 1 else 0 end) as n5m5");
            sql.append("      ,sum( case when sales_value between " + getRankItemU4() + " and " + (getRankItemU5() - 1) + " then 1 else 0 end) as n5m4");
            sql.append("      ,sum( case when sales_value between " + getRankItemU3() + " and " + (getRankItemU4() - 1) + " then 1 else 0 end) as n5m3");
            sql.append("      ,sum(  case when sales_value between " + getRankItemU2() + " and " + (getRankItemU3() - 1) + " then 1 else 0 end) as n5m2");
            sql.append("      ,sum(  case when sales_value between " + getRankItemU1() + " and " + (getRankItemU2() - 1) + " then 1 else 0 end) as n5m1");
        }

        sql.append(" from");
        sql.append("     wk_rank" + typeOfRank);
        if (typeOfRank == RANK_TECHNIC) {
            sql.append("  where");
            sql.append(" date_num < " + getCycleDate() + "");
        }
        //IVS_LVTu start add 2015/05/22 New request #36880
        if(this.getShopId() != null || this.getStaffId() != null) {
            if (typeOfRank == RANK_TECHNIC) {
                sql.append(" AND ");
            }
            if ( typeOfRank == RANK_ITEM_USE || typeOfRank == 3) {
                sql.append("  where ");
            }
            sql.append(" customer_id IN (SELECT distinct customer_id ");
            sql.append(" from");
            sql.append("     view_data_sales_detail_valid a");
            sql.append("     join mst_customer b using (customer_id)");
            sql.append(" where");
            sql.append("      a.sales_date between " + getTermFrom() + " and " + getTermTo());
            sql.append("     and b.customer_no <> '0'");
            if ( this.getShopId() != null ) {
                sql.append(" and a.shop_id = " + this.getShopId());
            }
            if ( this.getStaffId() != null ) {
                sql.append(" and a.staff_id = " + this.getStaffId());
            }
            if (typeOfRank == 1) {
                //技術・技術クレーム
                if (mstRankAdS.getSales_flag() != null && mstRankAdS.getSales_flag() == 1) {
                    sql.append(" and a.product_division in (1,2,3,4)");
                } else {
                    sql.append(" and a.product_division in (1,3)");
                }
            } else {
                //商品・商品返品
                sql.append(" and a.product_division in (2,4)");
            }
            if(typeOfRank == 3)
            {
                sql.append(" AND a.customer_id not in (select customer_id from wk_rank2) ");
            }
            if (typeOfRank == 2) {
                sql.append(" AND a.product_id");
                sql.append("    IN");
                sql.append("    ( SELECT item_id FROM mst_rank_item_detail WHERE item_group_id ");
                sql.append("         IN (" + itemGroupId  + "))");
            }

            if (typeOfRank == 3) {
                sql.append(" AND a.product_id");
                sql.append("     IN");
                sql.append("        (SELECT item_id FROM mst_rank_item_detail WHERE item_group_id");
                sql.append("             IN ( " + itemSaleGroup + " ))");
            }
            if (this.getShopCagegory() != 0) {
                if (typeOfRank == RANK_TECHNIC) {
                    sql.append(" and \n");
                        sql.append(" (	\n");
                        sql.append(" exists (	\n");
                        sql.append("         select 1 from \n");
                        sql.append("         data_sales_detail ds1\n");
                        sql.append("         inner join mst_technic mt on ds1.product_id = mt.technic_id and a.product_division = ds1.product_division and ds1.product_division in (1,3) \n");
                        sql.append("         inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id \n");
                        sql.append("         where ds1.slip_no = a.slip_no and ds1.shop_id = a.shop_id and ds1.slip_detail_no = a.slip_detail_no \n");
                        sql.append("         and mtc.shop_category_id = " + this.getShopCagegory() + "	\n");
                        sql.append("         ) 	\n");
                        if (mstRankAdS.getSales_flag() != null && mstRankAdS.getSales_flag() == 1) {
                            sql.append(" or \n");
                            sql.append(" exists(\n");
                            sql.append("         select 1 from\n");
                            sql.append("         data_sales_detail ds1\n");
                            sql.append("         inner join mst_item mi on ds1.product_id = mi.item_id and a.product_division = ds1.product_division and ds1.product_division in (2,4) \n");
                            sql.append("         inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                            sql.append("         where ds1.slip_no = a.slip_no and ds1.shop_id = a.shop_id and ds1.slip_detail_no = a.slip_detail_no\n");
                            sql.append("         and mic.shop_category_id = " + this.getShopCagegory() + "\n");
                            sql.append("         )\n");
                        }
                        sql.append(" )\n");
                } else {
                    sql.append(" and \n");
                    sql.append(" exists(\n");
                    sql.append("         select 1 from\n");
                    sql.append("         data_sales_detail ds1\n");
                    sql.append("         inner join mst_item mi on ds1.product_id = mi.item_id and a.product_division = ds1.product_division and ds1.product_division in (2,4) \n");
                    sql.append("         inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                    sql.append("         where ds1.slip_no = a.slip_no and ds1.shop_id = a.shop_id and ds1.slip_detail_no = a.slip_detail_no\n");
                    sql.append("         and mic.shop_category_id = " + this.getShopCagegory() + "\n");
                    sql.append("         )\n");
                }
            }
            sql.append(" group by");
            sql.append("     a.customer_id");
            sql.append(" HAVING SUM(" + salesValueColumn + ")  >= coalesce( ");
            sql.append("                                    (SELECT  ");
             if (typeOfRank == 1) {
                sql.append("technic_value1 ");
             }
             else
             {
                 sql.append("item_value1 ");
             }
            sql.append("                                     FROM mst_rank_advanced_setting ");
            sql.append("                                     WHERE shop_category_id =" + this.getShopCagegory() + "),0) ) ");
        }
        //IVS_LVTu end add 2015/05/22 New request #36880
        rs = con.executeQuery(sql.toString());
        if (rs.next()) {
            if (typeOfRank == RANK_TECHNIC) {
                CustomerRanking temp = null;

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f5m5"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f5m4"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f5m3"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f5m2"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f5m1"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f4m5"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f4m4"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f4m3"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f4m2"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f4m1"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f3m5"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f3m4"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f3m3"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f3m2"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f3m1"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f2m5"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f2m4"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f2m3"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f2m2"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f2m1"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f1m5"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f1m4"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f1m3"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f1m2"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f1m1"));
                this.add(temp);
            } else if (typeOfRank == RANK_ITEM_USE) {
                CustomerRanking temp = null;

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("i5m5"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("i5m4"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("i5m3"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("i5m2"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("i5m1"));
                this.add(temp);
            } else {
                CustomerRanking temp = null;

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("n5m5"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("n5m4"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("n5m3"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("n5m2"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("n5m1"));
                this.add(temp);
            }

        }

        rs.close();
    }

    /**
     * Create temp table to rank item 
     * @param shopCategoryId
     * @param typeOfRank
     * @throws Exception 
     */
    public void createTempTargetList(int shopCategoryId, int typeOfRank) throws Exception {
        ConnectionWrapper con = SystemInfo.getConnection();
        String itemSaleGroup = null;
        String itemGroupId = null;
        try {
            ResultSetWrapper rs = null;
            String sql = "SELECT item_sales_group, item_group_id FROM mst_rank_advanced_setting WHERE shop_category_id = " + shopCategoryId;
            rs = con.executeQuery(sql.toString());
            if (rs.next()) {
                itemSaleGroup = rs.getString("item_sales_group");
                itemGroupId = rs.getString("item_group_id");
            }
        } catch (Exception e) {
         }
        
        try {
            con.execute("drop table wk_rank" + typeOfRank);
        } catch (Exception e) {
        }
        //IVS_LVTu start add 2015/04/20 New request #36344
        MstRankAdvancedSetting mstRankAdS = new MstRankAdvancedSetting();
        if (typeOfRank == 1) {
            mstRankAdS.setShopCategoryId(shopCategoryId);
            mstRankAdS.loadRankAdvancedSettingData();
        }
        //IVS_LVTu end add 2015/04/20 New request #36344
        StringBuilder sql = new StringBuilder(1000);

        sql.append(" create temporary table wk_rank" + typeOfRank +" as ");
        sql.append(" select");
        sql.append("      a.customer_id");
        sql.append("     ,max(coalesce(b.customer_no, '')) as customer_no");
        sql.append("     ,max(coalesce(b.customer_name1, '') || '　' || coalesce(b.customer_name2, '')) as customer_name");
        sql.append("     ,max(coalesce(b.pc_mail_address, '')) as pc_mail_address");
        sql.append("     ,max(coalesce(b.cellular_mail_address, '')) as cellular_mail_address");
        sql.append("     ,max(a.sales_date) as sales_date ");
        sql.append(", " + getCycleDate() + " /count(DISTINCT slip_no)  AS date_num");
        sql.append("     ,(");
        sql.append("         select");
        sql.append("             ms.staff_name1 || '　' || ms.staff_name2");
        sql.append("         from");
        sql.append("             data_sales ds");
        sql.append("                 join mst_staff ms");
        sql.append("                 using(staff_id)");
        sql.append("         where");
        sql.append("                 ds.customer_id = a.customer_id");
        sql.append("             and ds.sales_date = max(a.sales_date)");
        sql.append("             and ds.delete_date is null");
        sql.append("         order by");
        sql.append("              ds.insert_date desc");
        sql.append("             ,ds.slip_no desc");
        sql.append("         limit 1");
        sql.append("      ) as staff_name");
        sql.append("     ,(");
        sql.append("         select");
        sql.append("             designated_flag");
        sql.append("         from");
        sql.append("             data_sales");
        sql.append("         where");
        sql.append("                 customer_id = a.customer_id");
        sql.append("             and sales_date = max(a.sales_date)");
        sql.append("             and delete_date is null");
        sql.append("         order by");
        sql.append("              insert_date desc");
        sql.append("             ,slip_no desc");
        sql.append("         limit 1");
        sql.append("      ) as designated_flag");
        sql.append("     ,count(distinct slip_no) as visit_count");  
        // Delete Start IVS_Hoa 2015/06/01
//        sql.append("    ,(");
//        sql.append("        select count(sc.customer_id)");
//        sql.append("         from");
//        sql.append("             mst_customer c");
//        sql.append("             ,(select customer_id");
//        sql.append("                 from data_sales");
//        sql.append("                where");
//        sql.append("                    sales_date between " + getTermFrom() + " and " + getTermTo());
//        sql.append("                 group by customer_id");
//        sql.append("               )as sc");
//        sql.append("        where");
//        sql.append("                c.introducer_id = a.customer_id");
//        sql.append("            and c.customer_id = sc.customer_id");
//        sql.append("            and c.customer_no <> '0'");
//        sql.append("        ) as introducer_cnt");
          // Delete End IVS_Hoa 2015/06/01

        String salesValueColumn = "";
        if (getTaxType() == TAX_TYPE_BLANK) {
            //割引後税抜き金額
            salesValueColumn = "discount_detail_value_no_tax";
        } else {
            //割引後税込み金額
            salesValueColumn = "discount_detail_value_in_tax";
        }
        sql.append(" ,    sum(" + salesValueColumn + ") as sales_value " );
        sql.append(" from");
        sql.append("     view_data_sales_detail_valid a");
        sql.append("     join mst_customer b using (customer_id)");
        //IVS_LVTu start delete 2015/04/20 New request #36344
//        if (shopCategoryId != 0) {
//            if (typeOfRank == RANK_TECHNIC) {
//                sql.append(" inner join mst_technic mt on mt.technic_id = a.product_id");
//                sql.append(" inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id");
//            } else {
//                sql.append(" inner join mst_item mt on mt.item_id = a.product_id");
//                sql.append(" inner join mst_item_class mtc on mtc.item_class_id = mt.item_class_id");
//            }
//        }
        //IVS_LVTu end delete 2015/04/20 New request #36344
        sql.append(" where");
        sql.append("      a.sales_date between " + getTermFrom() + " and " + getTermTo());
        sql.append("     and b.customer_no <> '0'");

        if (typeOfRank == 1) {
            //技術・技術クレーム
            //IVS_LVTu start add 2015/04/20 New request #36344
            if (mstRankAdS.getSales_flag() != null && mstRankAdS.getSales_flag() == 1) {
                sql.append(" and a.product_division in (1,2,3,4)");
            } else {
                sql.append(" and a.product_division in (1,3)");
            }
            //IVS_LVTu end add 2015/04/20 New request #36344
        } else {
            //商品・商品返品
            sql.append(" and a.product_division in (2,4)");
        }
        //IVS_LVTu start delete 2015/04/20 New request #36344
//        if (shopCategoryId != 0) {
//            sql.append(" AND mtc.shop_category_id = " + shopCategoryId + "");
//        }
        //IVS_LVTu end delete 2015/04/20 New request #36344
        if(typeOfRank == 3)
        {
            sql.append(" AND a.customer_id not in (select customer_id from wk_rank2) ");
        }
        if (typeOfRank == 2) {
            sql.append(" AND a.product_id");
            sql.append("    IN");
            sql.append("    ( SELECT item_id FROM mst_rank_item_detail WHERE item_group_id ");
            //IVS_LVTu start edit 2015/02/27 new request ( MASHU様独自ランク設計書_20150224)
            sql.append("         IN (" + itemGroupId  + "))");
            //sql.append("         (SELECT item_group_id FROM mst_rank_advanced_setting WHERE shop_category_id = " + shopCategoryId + "  )");
            //sql.append("         )");
            if (listItemGroupId.size() > 1){
                for ( int i = 0 ; i < listItemGroupId.size() ; i++){
                sql.append("  AND a.customer_id in (select DISTINCT customer_id from wk_rank2_" +listItemGroupId.get(i) + ")" );
                }
            }
            //IVS_LVTu end edit 2015/02/27 new request ( MASHU様独自ランク設計書_20150224)
        }

        if (typeOfRank == 3) {
            sql.append(" AND a.product_id");
            sql.append("     IN");
            sql.append("        (SELECT item_id FROM mst_rank_item_detail WHERE item_group_id");
            sql.append("             IN ( " + itemSaleGroup + " ))");
            //IVS_LVTu start delete 2015/02/27 new request ( MASHU様独自ランク設計書_20150224)
            //sql.append("             AND item_group_id NOT IN ( " + itemGroupId + " )");
            //sql.append(" )");
//            sql.append(" AND a.product_id");
//            sql.append("    NOT IN");
//            sql.append("    ( SELECT item_id FROM mst_rank_item_detail WHERE item_group_id ");
//            sql.append("         IN");
//            sql.append("         (SELECT item_group_id FROM mst_rank_advanced_setting WHERE shop_category_id = " + shopCategoryId + "  )");
//            sql.append("         )");
            //IVS_LVTu end delete 2015/02/27 new request ( MASHU様独自ランク設計書_20150224)
        }
        //IVS_LVTu start add 2015/04/20 New request #36344
        if (shopCategoryId != 0) {
            if (typeOfRank == RANK_TECHNIC) {
                sql.append(" and \n");
                    sql.append(" (	\n");
                    sql.append(" exists (	\n");
                    sql.append("         select 1 from \n");
                    sql.append("         data_sales_detail ds1\n");
                    sql.append("         inner join mst_technic mt on ds1.product_id = mt.technic_id and a.product_division = ds1.product_division and ds1.product_division in (1,3) \n");
                    sql.append("         inner join mst_technic_class mtc on mtc.technic_class_id = mt.technic_class_id \n");
                    sql.append("         where ds1.slip_no = a.slip_no and ds1.shop_id = a.shop_id and ds1.slip_detail_no = a.slip_detail_no \n");
                    sql.append("         and mtc.shop_category_id = " + shopCategoryId + "	\n");
                    sql.append("         ) 	\n");
                    if (mstRankAdS.getSales_flag() != null && mstRankAdS.getSales_flag() == 1) {
                        sql.append(" or \n");
                        sql.append(" exists(\n");
                        sql.append("         select 1 from\n");
                        sql.append("         data_sales_detail ds1\n");
                        sql.append("         inner join mst_item mi on ds1.product_id = mi.item_id and a.product_division = ds1.product_division and ds1.product_division in (2,4) \n");
                        sql.append("         inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                        sql.append("         where ds1.slip_no = a.slip_no and ds1.shop_id = a.shop_id and ds1.slip_detail_no = a.slip_detail_no\n");
                        sql.append("         and mic.shop_category_id = " + shopCategoryId + "\n");
                        sql.append("         )\n");
                    }
                    sql.append(" )\n");
            } else {
                sql.append(" and \n");
                sql.append(" exists(\n");
                sql.append("         select 1 from\n");
                sql.append("         data_sales_detail ds1\n");
                sql.append("         inner join mst_item mi on ds1.product_id = mi.item_id and a.product_division = ds1.product_division and ds1.product_division in (2,4) \n");
                sql.append("         inner join mst_item_class mic on mic.item_class_id = mi.item_class_id\n");
                sql.append("         where ds1.slip_no = a.slip_no and ds1.shop_id = a.shop_id and ds1.slip_detail_no = a.slip_detail_no\n");
                sql.append("         and mic.shop_category_id = " + shopCategoryId + "\n");
                sql.append("         )\n");
            }
        }
        //IVS_LVTu end add 2015/04/20 New request #36344
        sql.append(" group by");
        sql.append("     a.customer_id");
        sql.append(" HAVING SUM(" + salesValueColumn + ")  >= coalesce( ");
        sql.append("                                    (SELECT  ");
         if (typeOfRank == 1) {
            sql.append("technic_value1 ");
         }
         else
         {
             sql.append("item_value1 ");
         }
        sql.append("                                     FROM mst_rank_advanced_setting ");
        sql.append("                                     WHERE shop_category_id =" + shopCategoryId + "),0) ");
        if (typeOfRank == 1) {
            sql.append(" AND (" + getCycleDate() + " / count(DISTINCT slip_no) )   < " + getCycleDate());
        }
        sql.append(" order by");
        if(typeOfRank == 1)
        {
            sql.append("     sales_value desc, date_num asc ");
        }
        else
        {
            sql.append("     sales_value desc ");
        }
        
        con.execute(sql.toString());
        //drop temporary table.
        //IVS_LVTu start add 2015/02/27 new request ( MASHU様独自ランク設計書_20150224)
        if (listItemGroupId.size() > 1 && typeOfRank == 2) {
            for (int i = 0; i < listItemGroupId.size(); i++) {
                sql = new StringBuilder(1000);
                try {
                    sql.append(" drop table wk_rank2_" + listItemGroupId.get(i));
                } catch (Exception e) {
                }
                con.execute(sql.toString());
            }
        }
        //IVS_LVTu end add 2015/02/27 new request ( MASHU様独自ランク設計書_20150224)
    }
    
    /**
     * Create temp table to rank item 
     * @param shopCategoryId
     * @param typeOfRank
     * @throws Exception 
     */
    //IVS_LVTu start add 2015/02/27 new request ( MASHU様独自ランク設計書_20150224)
    public void createTempTargetList2(int shopCategoryId, int typeOfRank, ArrayList<Integer> itemGroupIdArr) throws Exception {
        ConnectionWrapper con = SystemInfo.getConnection();
        try {

            for (int i = 0; i < itemGroupIdArr.size(); i++) {
                con.execute("drop table wk_rank" + typeOfRank + "_" + itemGroupIdArr.get(i));
            }

        } catch (Exception e) {
        }

        StringBuilder sql = new StringBuilder(1000);
        for (int i = 0; i < itemGroupIdArr.size(); i++) {
            sql = new StringBuilder(1000);
            sql.append(" create temporary table wk_rank" + typeOfRank + "_" + itemGroupIdArr.get(i) + "  as ");
            sql.append(" select");
            sql.append("      DISTINCT a.customer_id");
            sql.append(" from");
            sql.append("     view_data_sales_detail_valid a");
            sql.append("     join mst_customer b using (customer_id)");
            if (shopCategoryId != 0) {
                sql.append(" inner join mst_item mt on mt.item_id = a.product_id");
                sql.append(" inner join mst_item_class mtc on mtc.item_class_id = mt.item_class_id");
            }
            sql.append(" where");
            sql.append("      a.sales_date between " + getTermFrom() + " and " + getTermTo());
            sql.append("     and b.customer_no <> '0'");
            //商品・商品返品
            sql.append(" and a.product_division in (2,4)");
            if (shopCategoryId != 0) {
                sql.append(" AND mtc.shop_category_id = " + shopCategoryId + "");
            }
            sql.append(" AND a.product_id");
            sql.append("    IN");
            sql.append("    ( SELECT item_id FROM mst_rank_item_detail WHERE item_group_id = " + itemGroupIdArr.get(i) + ")");

            con.execute(sql.toString());
        }
    }

    //IVS_LVTu end add 2015/02/27 new request ( MASHU様独自ランク設計書_20150224)
    public boolean deleteCustomerRank(int shopCategoryId){
        String sql = "";
        sql = " Delete from mst_customer_rank where shop_category_id = "+ shopCategoryId;
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            con.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(MasherRanking5List.class.getName()).log(Level.SEVERE, null, ex);
            return false; 
        }
        return true;
    }
    
    /**
     * @return the rankR3C2
     */
    public String getRankR3C2() {
        return rankR3C2;
    }

    /**
     * @param rankR3C2 the rankR3C2 to set
     */
    public void setRankR3C2(String rankR3C2) {
        this.rankR3C2 = rankR3C2;
    }

    /**
     * @return the rankR3C3
     */
    public String getRankR3C3() {
        return rankR3C3;
    }

    /**
     * @param rankR3C3 the rankR3C3 to set
     */
    public void setRankR3C3(String rankR3C3) {
        this.rankR3C3 = rankR3C3;
    }

    /**
     * @return the rankR3C4
     */
    public String getRankR3C4() {
        return rankR3C4;
    }

    /**
     * @param rankR3C4 the rankR3C4 to set
     */
    public void setRankR3C4(String rankR3C4) {
        this.rankR3C4 = rankR3C4;
    }

    /**
     * @return the rankR3C5
     */
    public String getRankR3C5() {
        return rankR3C5;
    }

    /**
     * @param rankR3C5 the rankR3C5 to set
     */
    public void setRankR3C5(String rankR3C5) {
        this.rankR3C5 = rankR3C5;
    }

    /**
     * @return the rankR3C6
     */
    public String getRankR3C6() {
        return rankR3C6;
    }

    /**
     * @param rankR3C6 the rankR3C6 to set
     */
    public void setRankR3C6(String rankR3C6) {
        this.rankR3C6 = rankR3C6;
    }

    /**
     * @return the rankR4C2
     */
    public String getRankR4C2() {
        return rankR4C2;
    }

    /**
     * @param rankR4C2 the rankR4C2 to set
     */
    public void setRankR4C2(String rankR4C2) {
        this.rankR4C2 = rankR4C2;
    }

    /**
     * @return the rankR4C3
     */
    public String getRankR4C3() {
        return rankR4C3;
    }

    /**
     * @param rankR4C3 the rankR4C3 to set
     */
    public void setRankR4C3(String rankR4C3) {
        this.rankR4C3 = rankR4C3;
    }

    /**
     * @return the rankR4C4
     */
    public String getRankR4C4() {
        return rankR4C4;
    }

    /**
     * @param rankR4C4 the rankR4C4 to set
     */
    public void setRankR4C4(String rankR4C4) {
        this.rankR4C4 = rankR4C4;
    }

    /**
     * @return the rankR4C5
     */
    public String getRankR4C5() {
        return rankR4C5;
    }

    /**
     * @param rankR4C5 the rankR4C5 to set
     */
    public void setRankR4C5(String rankR4C5) {
        this.rankR4C5 = rankR4C5;
    }

    /**
     * @return the rankR4C6
     */
    public String getRankR4C6() {
        return rankR4C6;
    }

    /**
     * @param rankR4C6 the rankR4C6 to set
     */
    public void setRankR4C6(String rankR4C6) {
        this.rankR4C6 = rankR4C6;
    }

    /**
     * @return the rankR5C2
     */
    public String getRankR5C2() {
        return rankR5C2;
    }

    /**
     * @param rankR5C2 the rankR5C2 to set
     */
    public void setRankR5C2(String rankR5C2) {
        this.rankR5C2 = rankR5C2;
    }

    /**
     * @return the rankR5C3
     */
    public String getRankR5C3() {
        return rankR5C3;
    }

    /**
     * @param rankR5C3 the rankR5C3 to set
     */
    public void setRankR5C3(String rankR5C3) {
        this.rankR5C3 = rankR5C3;
    }

    /**
     * @return the rankR5C4
     */
    public String getRankR5C4() {
        return rankR5C4;
    }

    /**
     * @param rankR5C4 the rankR5C4 to set
     */
    public void setRankR5C4(String rankR5C4) {
        this.rankR5C4 = rankR5C4;
    }

    /**
     * @return the rankR5C5
     */
    public String getRankR5C5() {
        return rankR5C5;
    }

    /**
     * @param rankR5C5 the rankR5C5 to set
     */
    public void setRankR5C5(String rankR5C5) {
        this.rankR5C5 = rankR5C5;
    }

    /**
     * @return the rankR5C6
     */
    public String getRankR5C6() {
        return rankR5C6;
    }

    /**
     * @param rankR5C6 the rankR5C6 to set
     */
    public void setRankR5C6(String rankR5C6) {
        this.rankR5C6 = rankR5C6;
    }

    /**
     * @return the rankR6C2
     */
    public String getRankR6C2() {
        return rankR6C2;
    }

    /**
     * @param rankR6C2 the rankR6C2 to set
     */
    public void setRankR6C2(String rankR6C2) {
        this.rankR6C2 = rankR6C2;
    }

    /**
     * @return the rankR6C3
     */
    public String getRankR6C3() {
        return rankR6C3;
    }

    /**
     * @param rankR6C3 the rankR6C3 to set
     */
    public void setRankR6C3(String rankR6C3) {
        this.rankR6C3 = rankR6C3;
    }

    /**
     * @return the rankR6C4
     */
    public String getRankR6C4() {
        return rankR6C4;
    }

    /**
     * @param rankR6C4 the rankR6C4 to set
     */
    public void setRankR6C4(String rankR6C4) {
        this.rankR6C4 = rankR6C4;
    }

    /**
     * @return the rankR6C5
     */
    public String getRankR6C5() {
        return rankR6C5;
    }

    /**
     * @param rankR6C5 the rankR6C5 to set
     */
    public void setRankR6C5(String rankR6C5) {
        this.rankR6C5 = rankR6C5;
    }

    /**
     * @return the rankR6C6
     */
    public String getRankR6C6() {
        return rankR6C6;
    }

    /**
     * @param rankR6C6 the rankR6C6 to set
     */
    public void setRankR6C6(String rankR6C6) {
        this.rankR6C6 = rankR6C6;
    }

    /**
     * @return the rankR7C2
     */
    public String getRankR7C2() {
        return rankR7C2;
    }

    /**
     * @param rankR7C2 the rankR7C2 to set
     */
    public void setRankR7C2(String rankR7C2) {
        this.rankR7C2 = rankR7C2;
    }

    /**
     * @return the rankR7C3
     */
    public String getRankR7C3() {
        return rankR7C3;
    }

    /**
     * @param rankR7C3 the rankR7C3 to set
     */
    public void setRankR7C3(String rankR7C3) {
        this.rankR7C3 = rankR7C3;
    }

    /**
     * @return the rankR7C4
     */
    public String getRankR7C4() {
        return rankR7C4;
    }

    /**
     * @param rankR7C4 the rankR7C4 to set
     */
    public void setRankR7C4(String rankR7C4) {
        this.rankR7C4 = rankR7C4;
    }

    /**
     * @return the rankR7C5
     */
    public String getRankR7C5() {
        return rankR7C5;
    }

    /**
     * @param rankR7C5 the rankR7C5 to set
     */
    public void setRankR7C5(String rankR7C5) {
        this.rankR7C5 = rankR7C5;
    }

    /**
     * @return the rankR7C6
     */
    public String getRankR7C6() {
        return rankR7C6;
    }

    /**
     * @param rankR7C6 the rankR7C6 to set
     */
    public void setRankR7C6(String rankR7C6) {
        this.rankR7C6 = rankR7C6;
    }

    /**
     * @return the rankR9C2
     */
    public String getRankR9C2() {
        return rankR9C2;
    }

    /**
     * @param rankR9C2 the rankR9C2 to set
     */
    public void setRankR9C2(String rankR9C2) {
        this.rankR9C2 = rankR9C2;
    }

    /**
     * @return the rankR9C3
     */
    public String getRankR9C3() {
        return rankR9C3;
    }

    /**
     * @param rankR9C3 the rankR9C3 to set
     */
    public void setRankR9C3(String rankR9C3) {
        this.rankR9C3 = rankR9C3;
    }

    /**
     * @return the rankR9C4
     */
    public String getRankR9C4() {
        return rankR9C4;
    }

    /**
     * @param rankR9C4 the rankR9C4 to set
     */
    public void setRankR9C4(String rankR9C4) {
        this.rankR9C4 = rankR9C4;
    }

    /**
     * @return the rankR9C5
     */
    public String getRankR9C5() {
        return rankR9C5;
    }

    /**
     * @param rankR9C5 the rankR9C5 to set
     */
    public void setRankR9C5(String rankR9C5) {
        this.rankR9C5 = rankR9C5;
    }

    /**
     * @return the rankR9C6
     */
    public String getRankR9C6() {
        return rankR9C6;
    }

    /**
     * @param rankR9C6 the rankR9C6 to set
     */
    public void setRankR9C6(String rankR9C6) {
        this.rankR9C6 = rankR9C6;
    }

    /**
     * @return the rankR10C2
     */
    public String getRankR10C2() {
        return rankR10C2;
    }

    /**
     * @param rankR10C2 the rankR10C2 to set
     */
    public void setRankR10C2(String rankR10C2) {
        this.rankR10C2 = rankR10C2;
    }

    /**
     * @return the rankR10C3
     */
    public String getRankR10C3() {
        return rankR10C3;
    }

    /**
     * @param rankR10C3 the rankR10C3 to set
     */
    public void setRankR10C3(String rankR10C3) {
        this.rankR10C3 = rankR10C3;
    }

    /**
     * @return the rankR10C4
     */
    public String getRankR10C4() {
        return rankR10C4;
    }

    /**
     * @param rankR10C4 the rankR10C4 to set
     */
    public void setRankR10C4(String rankR10C4) {
        this.rankR10C4 = rankR10C4;
    }

    /**
     * @return the rankR10C5
     */
    public String getRankR10C5() {
        return rankR10C5;
    }

    /**
     * @param rankR10C5 the rankR10C5 to set
     */
    public void setRankR10C5(String rankR10C5) {
        this.rankR10C5 = rankR10C5;
    }

    /**
     * @return the rankR10C6
     */
    public String getRankR10C6() {
        return rankR10C6;
    }

    /**
     * @param rankR10C6 the rankR10C6 to set
     */
    public void setRankR10C6(String rankR10C6) {
        this.rankR10C6 = rankR10C6;
    }

    /**
     * @return the shopCagegoryId
     */
    public Integer getShopCagegory() {
        return shopCagegoryId;
    }

    /**
     * @param shopCagegory the shopCagegoryId to set
     */
    public void setShopCagegory(Integer shopCagegory) {
        this.shopCagegoryId = shopCagegory;
    }

    /**
     * @return the limitCount
     */
    public long getLimitCount() {
        return limitCount;
    }

    /**
     * @param limitCount the limitCount to set
     */
    public void setLimitCount(long limitCount) {
        this.limitCount = limitCount;
    }

    /**
     * @return the rankF5
     */
    public long getRankF5() {
        return rankF5;
    }

    /**
     * @param rankF5 the rankF5 to set
     */
    public void setRankF5(long rankF5) {
        this.rankF5 = rankF5;
    }

    /**
     * @return the rankF4
     */
    public long getRankF4() {
        return rankF4;
    }

    /**
     * @param rankF4 the rankF4 to set
     */
    public void setRankF4(long rankF4) {
        this.rankF4 = rankF4;
    }

    /**
     * @return the rankF3
     */
    public long getRankF3() {
        return rankF3;
    }

    /**
     * @param rankF3 the rankF3 to set
     */
    public void setRankF3(long rankF3) {
        this.rankF3 = rankF3;
    }

    /**
     * @return the rankF2
     */
    public long getRankF2() {
        return rankF2;
    }

    /**
     * @param rankF2 the rankF2 to set
     */
    public void setRankF2(long rankF2) {
        this.rankF2 = rankF2;
    }

    /**
     * @return the rankF1
     */
    public long getRankF1() {
        return rankF1;
    }

    /**
     * @param rankF1 the rankF1 to set
     */
    public void setRankF1(long rankF1) {
        this.rankF1 = rankF1;
    }

    /**
     * @return the rankM5
     */
    public long getRankM5() {
        return rankM5;
    }

    /**
     * @param rankM5 the rankM5 to set
     */
    public void setRankM5(long rankM5) {
        this.rankM5 = rankM5;
    }

    /**
     * @return the rankM4
     */
    public long getRankM4() {
        return rankM4;
    }

    /**
     * @param rankM4 the rankM4 to set
     */
    public void setRankM4(long rankM4) {
        this.rankM4 = rankM4;
    }

    /**
     * @return the rankM3
     */
    public long getRankM3() {
        return rankM3;
    }

    /**
     * @param rankM3 the rankM3 to set
     */
    public void setRankM3(long rankM3) {
        this.rankM3 = rankM3;
    }

    /**
     * @return the rankM2
     */
    public long getRankM2() {
        return rankM2;
    }

    /**
     * @param rankM2 the rankM2 to set
     */
    public void setRankM2(long rankM2) {
        this.rankM2 = rankM2;
    }

    /**
     * @return the rankM1
     */
    public long getRankM1() {
        return rankM1;
    }

    /**
     * @param rankM1 the rankM1 to set
     */
    public void setRankM1(long rankM1) {
        this.rankM1 = rankM1;
    }

    /**
     * @return the cycleDate
     */
    public long getCycleDate() {
        return cycleDate;
    }

    /**
     * @param cycleDate the cycleDate to set
     */
    public void setCycleDate(long cycleDate) {
        this.cycleDate = cycleDate;
    }

    /**
     * @return the rankU5
     */
    public long getRankU5() {
        return rankU5;
    }

    /**
     * @param rankU5 the rankU5 to set
     */
    public void setRankU5(long rankU5) {
        this.rankU5 = rankU5;
    }

    /**
     * @return the rankU4
     */
    public long getRankU4() {
        return rankU4;
    }

    /**
     * @param rankU4 the rankU4 to set
     */
    public void setRankU4(long rankU4) {
        this.rankU4 = rankU4;
    }

    /**
     * @return the rankU3
     */
    public long getRankU3() {
        return rankU3;
    }

    /**
     * @param rankU3 the rankU3 to set
     */
    public void setRankU3(long rankU3) {
        this.rankU3 = rankU3;
    }

    /**
     * @return the rankU2
     */
    public long getRankU2() {
        return rankU2;
    }

    /**
     * @param rankU2 the rankU2 to set
     */
    public void setRankU2(long rankU2) {
        this.rankU2 = rankU2;
    }

    /**
     * @return the rankU1
     */
    public long getRankU1() {
        return rankU1;
    }

    /**
     * @param rankU1 the rankU1 to set
     */
    public void setRankU1(long rankU1) {
        this.rankU1 = rankU1;
    }

    /**
     * @return the rankItemU5
     */
    public long getRankItemU5() {
        return rankItemU5;
    }

    /**
     * @param rankItemU5 the rankItemU5 to set
     */
    public void setRankItemU5(long rankItemU5) {
        this.rankItemU5 = rankItemU5;
    }

    /**
     * @return the rankItemU4
     */
    public long getRankItemU4() {
        return rankItemU4;
    }

    /**
     * @param rankItemU4 the rankItemU4 to set
     */
    public void setRankItemU4(long rankItemU4) {
        this.rankItemU4 = rankItemU4;
    }

    /**
     * @return the rankItemU3
     */
    public long getRankItemU3() {
        return rankItemU3;
    }

    /**
     * @param rankItemU3 the rankItemU3 to set
     */
    public void setRankItemU3(long rankItemU3) {
        this.rankItemU3 = rankItemU3;
    }

    /**
     * @return the rankItemU2
     */
    public long getRankItemU2() {
        return rankItemU2;
    }

    /**
     * @param rankItemU2 the rankItemU2 to set
     */
    public void setRankItemU2(long rankItemU2) {
        this.rankItemU2 = rankItemU2;
    }

    /**
     * @return the rankItemU1
     */
    public long getRankItemU1() {
        return rankItemU1;
    }

    /**
     * @param rankItemU1 the rankItemU1 to set
     */
    public void setRankItemU1(long rankItemU1) {
        this.rankItemU1 = rankItemU1;
    }

    /**
     * @return the selectedRow
     */
    public long getSelectedRow() {
        return selectedRow;
    }

    /**
     * @param selectedRow the selectedRow to set
     */
    public void setSelectedRow(long selectedRow) {
        this.selectedRow = selectedRow;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the rankR3C2Value
     */
    public long getRankR3C2Value() {
        return rankR3C2Value;
    }

    /**
     * @param rankR3C2Value the rankR3C2Value to set
     */
    public void setRankR3C2Value(long rankR3C2Value) {
        this.rankR3C2Value = rankR3C2Value;
    }

    /**
     * @return the rankR3C3Value
     */
    public long getRankR3C3Value() {
        return rankR3C3Value;
    }

    /**
     * @param rankR3C3Value the rankR3C3Value to set
     */
    public void setRankR3C3Value(long rankR3C3Value) {
        this.rankR3C3Value = rankR3C3Value;
    }

    /**
     * @return the rankR3C4Value
     */
    public long getRankR3C4Value() {
        return rankR3C4Value;
    }

    /**
     * @param rankR3C4Value the rankR3C4Value to set
     */
    public void setRankR3C4Value(long rankR3C4Value) {
        this.rankR3C4Value = rankR3C4Value;
    }

    /**
     * @return the rankR3C5Value
     */
    public long getRankR3C5Value() {
        return rankR3C5Value;
    }

    /**
     * @param rankR3C5Value the rankR3C5Value to set
     */
    public void setRankR3C5Value(long rankR3C5Value) {
        this.rankR3C5Value = rankR3C5Value;
    }

    /**
     * @return the rankR3C6Value
     */
    public long getRankR3C6Value() {
        return rankR3C6Value;
    }

    /**
     * @param rankR3C6Value the rankR3C6Value to set
     */
    public void setRankR3C6Value(long rankR3C6Value) {
        this.rankR3C6Value = rankR3C6Value;
    }

    /**
     * @return the rankR4C2Value
     */
    public long getRankR4C2Value() {
        return rankR4C2Value;
    }

    /**
     * @param rankR4C2Value the rankR4C2Value to set
     */
    public void setRankR4C2Value(long rankR4C2Value) {
        this.rankR4C2Value = rankR4C2Value;
    }

    /**
     * @return the rankR4C3Value
     */
    public long getRankR4C3Value() {
        return rankR4C3Value;
    }

    /**
     * @param rankR4C3Value the rankR4C3Value to set
     */
    public void setRankR4C3Value(long rankR4C3Value) {
        this.rankR4C3Value = rankR4C3Value;
    }

    /**
     * @return the rankR4C4Value
     */
    public long getRankR4C4Value() {
        return rankR4C4Value;
    }

    /**
     * @param rankR4C4Value the rankR4C4Value to set
     */
    public void setRankR4C4Value(long rankR4C4Value) {
        this.rankR4C4Value = rankR4C4Value;
    }

    /**
     * @return the rankR4C5Value
     */
    public long getRankR4C5Value() {
        return rankR4C5Value;
    }

    /**
     * @param rankR4C5Value the rankR4C5Value to set
     */
    public void setRankR4C5Value(long rankR4C5Value) {
        this.rankR4C5Value = rankR4C5Value;
    }

    /**
     * @return the rankR4C6Value
     */
    public long getRankR4C6Value() {
        return rankR4C6Value;
    }

    /**
     * @param rankR4C6Value the rankR4C6Value to set
     */
    public void setRankR4C6Value(long rankR4C6Value) {
        this.rankR4C6Value = rankR4C6Value;
    }

    /**
     * @return the rankR5C2Value
     */
    public long getRankR5C2Value() {
        return rankR5C2Value;
    }

    /**
     * @param rankR5C2Value the rankR5C2Value to set
     */
    public void setRankR5C2Value(long rankR5C2Value) {
        this.rankR5C2Value = rankR5C2Value;
    }

    /**
     * @return the rankR5C3Value
     */
    public long getRankR5C3Value() {
        return rankR5C3Value;
    }

    /**
     * @param rankR5C3Value the rankR5C3Value to set
     */
    public void setRankR5C3Value(long rankR5C3Value) {
        this.rankR5C3Value = rankR5C3Value;
    }

    /**
     * @return the rankR5C4Value
     */
    public long getRankR5C4Value() {
        return rankR5C4Value;
    }

    /**
     * @param rankR5C4Value the rankR5C4Value to set
     */
    public void setRankR5C4Value(long rankR5C4Value) {
        this.rankR5C4Value = rankR5C4Value;
    }

    /**
     * @return the rankR5C5Value
     */
    public long getRankR5C5Value() {
        return rankR5C5Value;
    }

    /**
     * @param rankR5C5Value the rankR5C5Value to set
     */
    public void setRankR5C5Value(long rankR5C5Value) {
        this.rankR5C5Value = rankR5C5Value;
    }

    /**
     * @return the rankR5C6Value
     */
    public long getRankR5C6Value() {
        return rankR5C6Value;
    }

    /**
     * @param rankR5C6Value the rankR5C6Value to set
     */
    public void setRankR5C6Value(long rankR5C6Value) {
        this.rankR5C6Value = rankR5C6Value;
    }

    /**
     * @return the rankR6C2Value
     */
    public long getRankR6C2Value() {
        return rankR6C2Value;
    }

    /**
     * @param rankR6C2Value the rankR6C2Value to set
     */
    public void setRankR6C2Value(long rankR6C2Value) {
        this.rankR6C2Value = rankR6C2Value;
    }

    /**
     * @return the rankR6C3Value
     */
    public long getRankR6C3Value() {
        return rankR6C3Value;
    }

    /**
     * @param rankR6C3Value the rankR6C3Value to set
     */
    public void setRankR6C3Value(long rankR6C3Value) {
        this.rankR6C3Value = rankR6C3Value;
    }

    /**
     * @return the rankR6C4Value
     */
    public long getRankR6C4Value() {
        return rankR6C4Value;
    }

    /**
     * @param rankR6C4Value the rankR6C4Value to set
     */
    public void setRankR6C4Value(long rankR6C4Value) {
        this.rankR6C4Value = rankR6C4Value;
    }

    /**
     * @return the rankR6C5Value
     */
    public long getRankR6C5Value() {
        return rankR6C5Value;
    }

    /**
     * @param rankR6C5Value the rankR6C5Value to set
     */
    public void setRankR6C5Value(long rankR6C5Value) {
        this.rankR6C5Value = rankR6C5Value;
    }

    /**
     * @return the rankR6C6Value
     */
    public long getRankR6C6Value() {
        return rankR6C6Value;
    }

    /**
     * @param rankR6C6Value the rankR6C6Value to set
     */
    public void setRankR6C6Value(long rankR6C6Value) {
        this.rankR6C6Value = rankR6C6Value;
    }

    /**
     * @return the rankR7C2Value
     */
    public long getRankR7C2Value() {
        return rankR7C2Value;
    }

    /**
     * @param rankR7C2Value the rankR7C2Value to set
     */
    public void setRankR7C2Value(long rankR7C2Value) {
        this.rankR7C2Value = rankR7C2Value;
    }

    /**
     * @return the rankR7C3Value
     */
    public long getRankR7C3Value() {
        return rankR7C3Value;
    }

    /**
     * @param rankR7C3Value the rankR7C3Value to set
     */
    public void setRankR7C3Value(long rankR7C3Value) {
        this.rankR7C3Value = rankR7C3Value;
    }

    /**
     * @return the rankR7C4Value
     */
    public long getRankR7C4Value() {
        return rankR7C4Value;
    }

    /**
     * @param rankR7C4Value the rankR7C4Value to set
     */
    public void setRankR7C4Value(long rankR7C4Value) {
        this.rankR7C4Value = rankR7C4Value;
    }

    /**
     * @return the rankR7C5Value
     */
    public long getRankR7C5Value() {
        return rankR7C5Value;
    }

    /**
     * @param rankR7C5Value the rankR7C5Value to set
     */
    public void setRankR7C5Value(long rankR7C5Value) {
        this.rankR7C5Value = rankR7C5Value;
    }

    /**
     * @return the rankR7C6Value
     */
    public long getRankR7C6Value() {
        return rankR7C6Value;
    }

    /**
     * @param rankR7C6Value the rankR7C6Value to set
     */
    public void setRankR7C6Value(long rankR7C6Value) {
        this.rankR7C6Value = rankR7C6Value;
    }

    /**
     * @return the rankR9C2Value
     */
    public long getRankR9C2Value() {
        return rankR9C2Value;
    }

    /**
     * @param rankR9C2Value the rankR9C2Value to set
     */
    public void setRankR9C2Value(long rankR9C2Value) {
        this.rankR9C2Value = rankR9C2Value;
    }

    /**
     * @return the rankR9C3Value
     */
    public long getRankR9C3Value() {
        return rankR9C3Value;
    }

    /**
     * @param rankR9C3Value the rankR9C3Value to set
     */
    public void setRankR9C3Value(long rankR9C3Value) {
        this.rankR9C3Value = rankR9C3Value;
    }

    /**
     * @return the rankR9C4Value
     */
    public long getRankR9C4Value() {
        return rankR9C4Value;
    }

    /**
     * @param rankR9C4Value the rankR9C4Value to set
     */
    public void setRankR9C4Value(long rankR9C4Value) {
        this.rankR9C4Value = rankR9C4Value;
    }

    /**
     * @return the rankR9C5Value
     */
    public long getRankR9C5Value() {
        return rankR9C5Value;
    }

    /**
     * @param rankR9C5Value the rankR9C5Value to set
     */
    public void setRankR9C5Value(long rankR9C5Value) {
        this.rankR9C5Value = rankR9C5Value;
    }

    /**
     * @return the rankR9C6Value
     */
    public long getRankR9C6Value() {
        return rankR9C6Value;
    }

    /**
     * @param rankR9C6Value the rankR9C6Value to set
     */
    public void setRankR9C6Value(long rankR9C6Value) {
        this.rankR9C6Value = rankR9C6Value;
    }

    /**
     * @return the rankR10C2Value
     */
    public long getRankR10C2Value() {
        return rankR10C2Value;
    }

    /**
     * @param rankR10C2Value the rankR10C2Value to set
     */
    public void setRankR10C2Value(long rankR10C2Value) {
        this.rankR10C2Value = rankR10C2Value;
    }

    /**
     * @return the rankR10C3Value
     */
    public long getRankR10C3Value() {
        return rankR10C3Value;
    }

    /**
     * @param rankR10C3Value the rankR10C3Value to set
     */
    public void setRankR10C3Value(long rankR10C3Value) {
        this.rankR10C3Value = rankR10C3Value;
    }

    /**
     * @return the rankR10C4Value
     */
    public long getRankR10C4Value() {
        return rankR10C4Value;
    }

    /**
     * @param rankR10C4Value the rankR10C4Value to set
     */
    public void setRankR10C4Value(long rankR10C4Value) {
        this.rankR10C4Value = rankR10C4Value;
    }

    /**
     * @return the rankR10C5Value
     */
    public long getRankR10C5Value() {
        return rankR10C5Value;
    }

    /**
     * @param rankR10C5Value the rankR10C5Value to set
     */
    public void setRankR10C5Value(long rankR10C5Value) {
        this.rankR10C5Value = rankR10C5Value;
    }

    /**
     * @return the rankR10C6Value
     */
    public long getRankR10C6Value() {
        return rankR10C6Value;
    }

    /**
     * @param rankR10C6Value the rankR10C6Value to set
     */
    public void setRankR10C6Value(long rankR10C6Value) {
        this.rankR10C6Value = rankR10C6Value;
    }

    /**
     * @return the technicRankId
     */
    public long getTechnicRankId() {
        return technicRankId;
    }

    /**
     * @param technicRankId the technicRankId to set
     */
    public void setTechnicRankId(long technicRankId) {
        this.technicRankId = technicRankId;
    }

    /**
     * @return the itemRankId
     */
    public long getItemRankId() {
        return itemRankId;
    }

    /**
     * @param itemRankId the itemRankId to set
     */
    public void setItemRankId(long itemRankId) {
        this.itemRankId = itemRankId;
    }

    /**
     * @return the rankR3C2Color
     */
    public String getRankR3C2Color() {
        return rankR3C2Color;
    }

    /**
     * @param rankR3C2Color the rankR3C2Color to set
     */
    public void setRankR3C2Color(String rankR3C2Color) {
        this.rankR3C2Color = rankR3C2Color;
    }

    /**
     * @return the rankR3C3Color
     */
    public String getRankR3C3Color() {
        return rankR3C3Color;
    }

    /**
     * @param rankR3C3Color the rankR3C3Color to set
     */
    public void setRankR3C3Color(String rankR3C3Color) {
        this.rankR3C3Color = rankR3C3Color;
    }

    /**
     * @return the rankR3C4Color
     */
    public String getRankR3C4Color() {
        return rankR3C4Color;
    }

    /**
     * @param rankR3C4Color the rankR3C4Color to set
     */
    public void setRankR3C4Color(String rankR3C4Color) {
        this.rankR3C4Color = rankR3C4Color;
    }

    /**
     * @return the rankR3C5Color
     */
    public String getRankR3C5Color() {
        return rankR3C5Color;
    }

    /**
     * @param rankR3C5Color the rankR3C5Color to set
     */
    public void setRankR3C5Color(String rankR3C5Color) {
        this.rankR3C5Color = rankR3C5Color;
    }

    /**
     * @return the rankR3C6Color
     */
    public String getRankR3C6Color() {
        return rankR3C6Color;
    }

    /**
     * @param rankR3C6Color the rankR3C6Color to set
     */
    public void setRankR3C6Color(String rankR3C6Color) {
        this.rankR3C6Color = rankR3C6Color;
    }

    /**
     * @return the rankR4C2Color
     */
    public String getRankR4C2Color() {
        return rankR4C2Color;
    }

    /**
     * @param rankR4C2Color the rankR4C2Color to set
     */
    public void setRankR4C2Color(String rankR4C2Color) {
        this.rankR4C2Color = rankR4C2Color;
    }

    /**
     * @return the rankR4C3Color
     */
    public String getRankR4C3Color() {
        return rankR4C3Color;
    }

    /**
     * @param rankR4C3Color the rankR4C3Color to set
     */
    public void setRankR4C3Color(String rankR4C3Color) {
        this.rankR4C3Color = rankR4C3Color;
    }

    /**
     * @return the rankR4C4Color
     */
    public String getRankR4C4Color() {
        return rankR4C4Color;
    }

    /**
     * @param rankR4C4Color the rankR4C4Color to set
     */
    public void setRankR4C4Color(String rankR4C4Color) {
        this.rankR4C4Color = rankR4C4Color;
    }

    /**
     * @return the rankR4C5Color
     */
    public String getRankR4C5Color() {
        return rankR4C5Color;
    }

    /**
     * @param rankR4C5Color the rankR4C5Color to set
     */
    public void setRankR4C5Color(String rankR4C5Color) {
        this.rankR4C5Color = rankR4C5Color;
    }

    /**
     * @return the rankR4C6Color
     */
    public String getRankR4C6Color() {
        return rankR4C6Color;
    }

    /**
     * @param rankR4C6Color the rankR4C6Color to set
     */
    public void setRankR4C6Color(String rankR4C6Color) {
        this.rankR4C6Color = rankR4C6Color;
    }

    /**
     * @return the rankR5C2Color
     */
    public String getRankR5C2Color() {
        return rankR5C2Color;
    }

    /**
     * @param rankR5C2Color the rankR5C2Color to set
     */
    public void setRankR5C2Color(String rankR5C2Color) {
        this.rankR5C2Color = rankR5C2Color;
    }

    /**
     * @return the rankR5C3Color
     */
    public String getRankR5C3Color() {
        return rankR5C3Color;
    }

    /**
     * @param rankR5C3Color the rankR5C3Color to set
     */
    public void setRankR5C3Color(String rankR5C3Color) {
        this.rankR5C3Color = rankR5C3Color;
    }

    /**
     * @return the rankR5C4Color
     */
    public String getRankR5C4Color() {
        return rankR5C4Color;
    }

    /**
     * @param rankR5C4Color the rankR5C4Color to set
     */
    public void setRankR5C4Color(String rankR5C4Color) {
        this.rankR5C4Color = rankR5C4Color;
    }

    /**
     * @return the rankR5C5Color
     */
    public String getRankR5C5Color() {
        return rankR5C5Color;
    }

    /**
     * @param rankR5C5Color the rankR5C5Color to set
     */
    public void setRankR5C5Color(String rankR5C5Color) {
        this.rankR5C5Color = rankR5C5Color;
    }

    /**
     * @return the rankR5C6Color
     */
    public String getRankR5C6Color() {
        return rankR5C6Color;
    }

    /**
     * @param rankR5C6Color the rankR5C6Color to set
     */
    public void setRankR5C6Color(String rankR5C6Color) {
        this.rankR5C6Color = rankR5C6Color;
    }

    /**
     * @return the rankR6C2Color
     */
    public String getRankR6C2Color() {
        return rankR6C2Color;
    }

    /**
     * @param rankR6C2Color the rankR6C2Color to set
     */
    public void setRankR6C2Color(String rankR6C2Color) {
        this.rankR6C2Color = rankR6C2Color;
    }

    /**
     * @return the rankR6C3Color
     */
    public String getRankR6C3Color() {
        return rankR6C3Color;
    }

    /**
     * @param rankR6C3Color the rankR6C3Color to set
     */
    public void setRankR6C3Color(String rankR6C3Color) {
        this.rankR6C3Color = rankR6C3Color;
    }

    /**
     * @return the rankR6C4Color
     */
    public String getRankR6C4Color() {
        return rankR6C4Color;
    }

    /**
     * @param rankR6C4Color the rankR6C4Color to set
     */
    public void setRankR6C4Color(String rankR6C4Color) {
        this.rankR6C4Color = rankR6C4Color;
    }

    /**
     * @return the rankR6C5Color
     */
    public String getRankR6C5Color() {
        return rankR6C5Color;
    }

    /**
     * @param rankR6C5Color the rankR6C5Color to set
     */
    public void setRankR6C5Color(String rankR6C5Color) {
        this.rankR6C5Color = rankR6C5Color;
    }

    /**
     * @return the rankR6C6Color
     */
    public String getRankR6C6Color() {
        return rankR6C6Color;
    }

    /**
     * @param rankR6C6Color the rankR6C6Color to set
     */
    public void setRankR6C6Color(String rankR6C6Color) {
        this.rankR6C6Color = rankR6C6Color;
    }

    /**
     * @return the rankR7C2Color
     */
    public String getRankR7C2Color() {
        return rankR7C2Color;
    }

    /**
     * @param rankR7C2Color the rankR7C2Color to set
     */
    public void setRankR7C2Color(String rankR7C2Color) {
        this.rankR7C2Color = rankR7C2Color;
    }

    /**
     * @return the rankR7C3Color
     */
    public String getRankR7C3Color() {
        return rankR7C3Color;
    }

    /**
     * @param rankR7C3Color the rankR7C3Color to set
     */
    public void setRankR7C3Color(String rankR7C3Color) {
        this.rankR7C3Color = rankR7C3Color;
    }

    /**
     * @return the rankR7C4Color
     */
    public String getRankR7C4Color() {
        return rankR7C4Color;
    }

    /**
     * @param rankR7C4Color the rankR7C4Color to set
     */
    public void setRankR7C4Color(String rankR7C4Color) {
        this.rankR7C4Color = rankR7C4Color;
    }

    /**
     * @return the rankR7C5Color
     */
    public String getRankR7C5Color() {
        return rankR7C5Color;
    }

    /**
     * @param rankR7C5Color the rankR7C5Color to set
     */
    public void setRankR7C5Color(String rankR7C5Color) {
        this.rankR7C5Color = rankR7C5Color;
    }

    /**
     * @return the rankR7C6Color
     */
    public String getRankR7C6Color() {
        return rankR7C6Color;
    }

    /**
     * @param rankR7C6Color the rankR7C6Color to set
     */
    public void setRankR7C6Color(String rankR7C6Color) {
        this.rankR7C6Color = rankR7C6Color;
    }

    /**
     * @return the rankR9C2Color
     */
    public String getRankR9C2Color() {
        return rankR9C2Color;
    }

    /**
     * @param rankR9C2Color the rankR9C2Color to set
     */
    public void setRankR9C2Color(String rankR9C2Color) {
        this.rankR9C2Color = rankR9C2Color;
    }

    /**
     * @return the rankR9C3Color
     */
    public String getRankR9C3Color() {
        return rankR9C3Color;
    }

    /**
     * @param rankR9C3Color the rankR9C3Color to set
     */
    public void setRankR9C3Color(String rankR9C3Color) {
        this.rankR9C3Color = rankR9C3Color;
    }

    /**
     * @return the rankR9C4Color
     */
    public String getRankR9C4Color() {
        return rankR9C4Color;
    }

    /**
     * @param rankR9C4Color the rankR9C4Color to set
     */
    public void setRankR9C4Color(String rankR9C4Color) {
        this.rankR9C4Color = rankR9C4Color;
    }

    /**
     * @return the rankR9C5Color
     */
    public String getRankR9C5Color() {
        return rankR9C5Color;
    }

    /**
     * @param rankR9C5Color the rankR9C5Color to set
     */
    public void setRankR9C5Color(String rankR9C5Color) {
        this.rankR9C5Color = rankR9C5Color;
    }

    /**
     * @return the rankR9C6Color
     */
    public String getRankR9C6Color() {
        return rankR9C6Color;
    }

    /**
     * @param rankR9C6Color the rankR9C6Color to set
     */
    public void setRankR9C6Color(String rankR9C6Color) {
        this.rankR9C6Color = rankR9C6Color;
    }

    /**
     * @return the rankR10C2Color
     */
    public String getRankR10C2Color() {
        return rankR10C2Color;
    }

    /**
     * @param rankR10C2Color the rankR10C2Color to set
     */
    public void setRankR10C2Color(String rankR10C2Color) {
        this.rankR10C2Color = rankR10C2Color;
    }

    /**
     * @return the rankR10C3Color
     */
    public String getRankR10C3Color() {
        return rankR10C3Color;
    }

    /**
     * @param rankR10C3Color the rankR10C3Color to set
     */
    public void setRankR10C3Color(String rankR10C3Color) {
        this.rankR10C3Color = rankR10C3Color;
    }

    /**
     * @return the rankR10C4Color
     */
    public String getRankR10C4Color() {
        return rankR10C4Color;
    }

    /**
     * @param rankR10C4Color the rankR10C4Color to set
     */
    public void setRankR10C4Color(String rankR10C4Color) {
        this.rankR10C4Color = rankR10C4Color;
    }

    /**
     * @return the rankR10C5Color
     */
    public String getRankR10C5Color() {
        return rankR10C5Color;
    }

    /**
     * @param rankR10C5Color the rankR10C5Color to set
     */
    public void setRankR10C5Color(String rankR10C5Color) {
        this.rankR10C5Color = rankR10C5Color;
    }

    /**
     * @return the rankR10C6Color
     */
    public String getRankR10C6Color() {
        return rankR10C6Color;
    }

    /**
     * @param rankR10C6Color the rankR10C6Color to set
     */
    public void setRankR10C6Color(String rankR10C6Color) {
        this.rankR10C6Color = rankR10C6Color;
    }

    /**
     * @return the itemGroupName
     */
    public String getItemGroupName() {
        return itemGroupName;
    }

    /**
     * @param itemGroupName the itemGroupName to set
     */
    public void setItemGroupName(String itemGroupName) {
        this.itemGroupName = itemGroupName;
    }
 
    private long selectedRow = 0l;
    private long limitCount = 0l;
    private long cycleDate = 0;  

    private GregorianCalendar termFrom = new GregorianCalendar();
    private GregorianCalendar termTo = new GregorianCalendar();

    private Integer shopCagegoryId = null;
    private Integer customerId = null;
    private long technicRankId = 0L;
    private long itemRankId = 0L;

    /**
     * 対象期間(開始日)を取得する。
     *
     * @return 対象期間(開始日)
     */
    public String getTermFrom() {
        return SQLUtil.convertForSQLDateOnly(termFrom);
    }

    /**
     * 対象期間(開始日)をセットする。
     *
     * @param termFrom 対象期間(開始日)
     */
    public void setTermFrom(java.util.Date termFrom) {
        this.termFrom.setTime(termFrom);
    }

    /**
     * 対象期間(終了日)を取得する。
     *
     * @return 対象期間(終了日)
     */
    public String getTermTo() {
        return SQLUtil.convertForSQLDateOnly(termTo);
    }

    /**
     * 対象期間(終了日)をセットする。
     *
     * @param termTo 対象期間(終了日)
     */
    public void setTermTo(java.util.Date termTo) {
        this.termTo.setTime(termTo);
    }

    /**
     * 税区分を取得する。
     *
     * @return 税区分
     */
    public int getTaxType() {
        return taxType;
    }

    /**
     * 税区分をセットする。
     *
     * @param taxType 税区分
     */
    public void setTaxType(int taxType) {
        this.taxType = taxType;
    }

    /**
     * 評価対象を取得する。
     *
     * @return 評価対象
     */
    public int getProductDivision() {
        return productDivision;
    }

    /**
     * 評価対象をセットする。
     *
     * @param productDivision 評価対象
     */
    public void setProductDivision(int productDivision) {
        this.productDivision = productDivision;
    }
    /**
     * 税区分
     */
    private Integer taxType = null;
    /**
     * 評価対象
     */
    private Integer productDivision = 1;

    private String rankR3C2 = "";
    private String rankR3C3 = "";
    private String rankR3C4 = "";
    private String rankR3C5 = "";
    private String rankR3C6 = "";
    private String rankR4C2 = "";
    private String rankR4C3 = "";
    private String rankR4C4 = "";
    private String rankR4C5 = "";
    private String rankR4C6 = "";
    private String rankR5C2 = "";
    private String rankR5C3 = "";
    private String rankR5C4 = "";
    private String rankR5C5 = "";
    private String rankR5C6 = "";
    private String rankR6C2 = "";
    private String rankR6C3 = "";
    private String rankR6C4 = "";
    private String rankR6C5 = "";
    private String rankR6C6 = "";
    private String rankR7C2 = "";
    private String rankR7C3 = "";
    private String rankR7C4 = "";
    private String rankR7C5 = "";
    private String rankR7C6 = "";
    private String rankR9C2 = "";
    private String rankR9C3 = "";
    private String rankR9C4 = "";
    private String rankR9C5 = "";
    private String rankR9C6 = "";
    private String rankR10C2 = "";
    private String rankR10C3 = "";
    private String rankR10C4 = "";
    private String rankR10C5 = "";
    private String rankR10C6 = "";

    private long rankR3C2Value = 0l;
    private long rankR3C3Value = 0l;
    private long rankR3C4Value = 0l;
    private long rankR3C5Value = 0l;
    private long rankR3C6Value = 0l;
    private long rankR4C2Value = 0l;
    private long rankR4C3Value = 0l;
    private long rankR4C4Value = 0l;
    private long rankR4C5Value = 0l;
    private long rankR4C6Value = 0l;
    private long rankR5C2Value = 0l;
    private long rankR5C3Value = 0l;
    private long rankR5C4Value = 0l;
    private long rankR5C5Value = 0l;
    private long rankR5C6Value = 0l;
    private long rankR6C2Value = 0l;
    private long rankR6C3Value = 0l;
    private long rankR6C4Value = 0l;
    private long rankR6C5Value = 0l;
    private long rankR6C6Value = 0l;
    private long rankR7C2Value = 0l;
    private long rankR7C3Value = 0l;
    private long rankR7C4Value = 0l;
    private long rankR7C5Value = 0l;
    private long rankR7C6Value = 0l;
    private long rankR9C2Value = 0l;
    private long rankR9C3Value = 0l;
    private long rankR9C4Value = 0l;
    private long rankR9C5Value = 0l;
    private long rankR9C6Value = 0l;
    private long rankR10C2Value = 0l;
    private long rankR10C3Value = 0l;
    private long rankR10C4Value = 0l;
    private long rankR10C5Value = 0l;
    private long rankR10C6Value = 0l;

    private String rankR3C2Color = "FFFFFF";
    private String rankR3C3Color = "FFFFFF";
    private String rankR3C4Color = "FFFFFF";
    private String rankR3C5Color = "FFFFFF";
    private String rankR3C6Color = "FFFFFF";
    private String rankR4C2Color = "FFFFFF";
    private String rankR4C3Color = "FFFFFF";
    private String rankR4C4Color = "FFFFFF";
    private String rankR4C5Color = "FFFFFF";
    private String rankR4C6Color = "FFFFFF";
    private String rankR5C2Color = "FFFFFF";
    private String rankR5C3Color = "FFFFFF";
    private String rankR5C4Color = "FFFFFF";
    private String rankR5C5Color = "FFFFFF";
    private String rankR5C6Color = "FFFFFF";
    private String rankR6C2Color = "FFFFFF";
    private String rankR6C3Color = "FFFFFF";
    private String rankR6C4Color = "FFFFFF";
    private String rankR6C5Color = "FFFFFF";
    private String rankR6C6Color = "FFFFFF";
    private String rankR7C2Color = "FFFFFF";
    private String rankR7C3Color = "FFFFFF";
    private String rankR7C4Color = "FFFFFF";
    private String rankR7C5Color = "FFFFFF";
    private String rankR7C6Color = "FFFFFF";
    private String rankR9C2Color = "FFFFFF";
    private String rankR9C3Color = "FFFFFF";
    private String rankR9C4Color = "FFFFFF";
    private String rankR9C5Color = "FFFFFF";
    private String rankR9C6Color = "FFFFFF";
    private String rankR10C2Color = "FFFFFF";
    private String rankR10C3Color = "FFFFFF";
    private String rankR10C4Color = "FFFFFF";
    private String rankR10C5Color = "FFFFFF";
    private String rankR10C6Color = "FFFFFF";

    //visit cycle and technic value rank
    private long rankF5 = 0l;

    private long rankF4 = 0l;

    private long rankF3 = 0l;

    private long rankF2 = 0l;

    private long rankF1 = 0l;

    private long rankM5 = 0l;

    private long rankM4 = 0l;

    private long rankM3 = 0l;

    private long rankM2 = 0l;

    private long rankM1 = 0l;

    //unit price
    private long rankU5 = 0l;
    private long rankU4 = 0l;
    private long rankU3 = 0l;
    private long rankU2 = 0l;
    private long rankU1 = 0l;

    //item value
    private long rankItemU5 = 0l;
    private long rankItemU4 = 0l;
    private long rankItemU3 = 0l;
    private long rankItemU2 = 0l;
    private long rankItemU1 = 0l;

    private String itemGroupName = "";
    //IVS_LVTu start edit 2015/02/27 new request ( MASHU様独自ランク設計書_20150224)
    private ArrayList<Integer> listItemGroupId = new  ArrayList<Integer>();
    
    public ArrayList<Integer> getItemGroupID(int shopCategoryId) {

        ArrayList<Integer> listItemGroupId = new  ArrayList<Integer>();
        ConnectionWrapper con = SystemInfo.getConnection();
        StringBuilder sqlQuery = new StringBuilder();
        String itemGroupID = "";
        sqlQuery.append(" select r.item_group_id ");
        sqlQuery.append(" from mst_rank_advanced_setting r");
        sqlQuery.append(" where r.shop_category_id = " + shopCategoryId + " ");

        try {
            ResultSetWrapper rs = null;
            rs = con.executeQuery(sqlQuery.toString());
            if (rs.next()) {
                itemGroupID = rs.getString("item_group_id");
            }else{
                this.setItemGroupName("");
                return null;
            }
        } catch (Exception e) {
        }
        try {
            ResultSetWrapper rs = null;
            sqlQuery = new StringBuilder();
            sqlQuery.append( " select item_group_id from  mst_rank_item_group where item_group_id in ( " + itemGroupID + " ) order by item_group_id " );
            rs = con.executeQuery(sqlQuery.toString());
            while (rs.next()) {
                listItemGroupId.add(rs.getInt("item_group_id"));
            }
        } catch (Exception e) {
        }
        return listItemGroupId;
    }
    //IVS_LVTu end edit 2015/02/27 new request ( MASHU様独自ランク設計書_20150224)
}

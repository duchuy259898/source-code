/*
 * ShopStaffs.java
 *
 * Created on 2008/10/04, 14:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.util.SQLUtil;
import java.util.*;
import java.sql.*;

import com.geobeck.sql.*;

/**
 * 在籍店舗スタッフデータ
 * @author trino
 */
public class ShopStaffs extends ArrayList<MstStaff>
{
    private java.util.Date yearMonth = null;
    
    /** Creates a new instance of ShopStaffs */
    public ShopStaffs(java.util.Date yearMonth) {
        this.yearMonth = yearMonth;
    }
    
    /**
     * スタッフを読み込む
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public boolean load(ConnectionWrapper con) throws SQLException {
        this.clear();
        
        ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
        
        while(rs.next()) {
            MstStaff		ms		=	new MstStaff();
            ms.setData(rs);
            ms.setStaffClassID(rs.getInt("staff_class_id"));
            
            // 在籍店舗データで上書き
            ms.setShopID(rs.getInt("dss_shop_id"));
            
            String shopName = rs.getString("dss_shop_name");
            ms.setShopName(rs.wasNull() ? "" : shopName);
            
            this.add(ms);
        }
        return	true;
    }
    
    public String getSelectSQL() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      ms.*");
        sql.append("     ,dss.shop_id   as dss_shop_id");
        sql.append("     ,mss.shop_name as dss_shop_name");
        sql.append(" from");
        sql.append("     mst_staff ms");
        sql.append("         left join data_shop_staff dss");
        sql.append("                on dss.staff_id = ms.staff_id");
        sql.append("               and dss.yearmonth =");
        sql.append("                     (");
        sql.append("                         select");
        sql.append("                             yearmonth");
        sql.append("                         from");
        sql.append("                             data_shop_staff");
        sql.append("                         where");
        sql.append("                             yearmonth <= " + SQLUtil.convertForSQL(yearMonth));
        sql.append("                         group by");
        sql.append("                             yearmonth");
        sql.append("                         order by");
        sql.append("                             yearmonth desc");
        sql.append("                         limit 1 ");
        sql.append("                     )");
        sql.append("               and dss.delete_date is null");
        sql.append("         left join mst_shop mss");
        sql.append("                on mss.shop_id = dss.shop_id");
        sql.append("               and mss.delete_date is null");
        sql.append(" where");
        sql.append("     ms.delete_date is null");
        sql.append(" order by");
        sql.append("      ms.display_seq");
        sql.append("     ,ms.staff_no");

        return sql.toString();
        
    }
    
    public boolean existData(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(this.getExistSQL());
        if(rs.next()) {
            if( rs.getInt("count") > 0 )
                return true;
        }
        return false;
    }
    
    public String getExistSQL() {
        return	"select count(*) as count \n" +
                 "from data_shop_staff \n" +
                 "where yearmonth = " + SQLUtil.convertForSQL(yearMonth) + "\n";
    }
    
    public int getIndexByID(Integer staffID) {
        for(int i = 0; i < this.size(); i ++) {
            MstStaff ms	=	this.get(i);
            if(ms.getStaffID() != null && ms.getStaffID().equals( staffID )) {
                return	i;
            }
        }
        
        return	-1;
    }

    public boolean update(ConnectionWrapper con) throws SQLException {
        for( MstStaff ms : this ){
            System.out.print(ms.getStaffName(0));
        }
        
        for(int i = 0; i < this.size(); i ++) {
            MstStaff ms	=	this.get(i);
            
            if( con.executeUpdate(getUpdateSQL(ms)) == 0 ) {
                if( con.executeUpdate(getInsertSQL(ms)) == 0 ) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public String getInsertSQL(MstStaff ms) {
        return 
            "insert into data_shop_staff \n" +
            "( \n" +
            "  yearmonth, \n" +
            "  staff_id, \n" +
            "  shop_id, \n" +
            "  insert_date, \n" +
            "  update_date \n" +
            ")select \n" +
            SQLUtil.convertForSQL(yearMonth) + ", \n" +
            SQLUtil.convertForSQL(ms.getStaffID()) + ", \n" +
            SQLUtil.convertForSQL(ms.getShopID()) + ", \n" +
            "  current_timestamp, \n" +
            "  current_timestamp";
    }
    
    public String getUpdateSQL(MstStaff ms) {
        return 
            "update data_shop_staff \n" +
            "set \n" +
            "  shop_id = " + SQLUtil.convertForSQL(ms.getShopID()) + ", \n" +
            "  update_date = current_timestamp \n" +
            "where \n" +
            "    yearmonth = " + SQLUtil.convertForSQL(yearMonth) + "\n" +
            "and staff_id  = " + SQLUtil.convertForSQL(ms.getStaffID()) + "\n";
    }
    
    
}

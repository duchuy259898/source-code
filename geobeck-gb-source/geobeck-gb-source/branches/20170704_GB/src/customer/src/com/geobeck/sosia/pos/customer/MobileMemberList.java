/*
 * MobileMemberList.java
 *
 * Created on 2009/09/08, 10:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author geobeck
 */
public class MobileMemberList extends ArrayList<MobileMemberData>
{
    private String  sosiaCode = null;
    private String  addDateFrom = null;
    private String  addDateTo = null;
    private Integer gearCondition = null;
    private String sosiaIdSearch;
    private String firstNameSearch;
    private String lastNameSearch;

    public String getSosiaIdSearch() {
        return sosiaIdSearch;
    }

    public void setSosiaIdSearch(String sosiaIdSearch) {
        this.sosiaIdSearch = sosiaIdSearch;
    }

    public String getFirstNameSearch() {
        return firstNameSearch;
    }

    public void setFirstNameSearch(String firstNameSearch) {
        this.firstNameSearch = firstNameSearch;
    }

    public String getLastNameSearch() {
        return lastNameSearch;
    }

    public void setLastNameSearch(String lastNameSearch) {
        this.lastNameSearch = lastNameSearch;
    }
    
    
    public String getSosiaCode() {
        return sosiaCode;
    }

    public void setSosiaCode(String sosiaCode) {
        this.sosiaCode = sosiaCode;
    }
    
    public String getAddDateFrom() {
        return addDateFrom;
    }

    public void setAddDateFrom(String addDateFrom) {
        this.addDateFrom = addDateFrom;
    }

    public String getAddDateTo() {
        return addDateTo;
    }

    public void setAddDateTo(String addDateTo) {
        this.addDateTo = addDateTo;
    }

    public Integer getGearCondition() {
        return gearCondition;
    }

    public void setGearCondition(Integer gearCondition) {
        this.gearCondition = gearCondition;
    }

    public MobileMemberList()
    {
    }

    public boolean load()
    {
        this.clear();

        try
        {
            ConnectionWrapper con = SystemInfo.getMobileConnection();

            ResultSetWrapper rs = con.executeQuery(this.getMobileMemberListSQL());

            Map sosiaIdList = new HashMap();
            sosiaIdList = getGearList(rs);
            rs.beforeFirst();
            
            while (rs.next()) {
                
                boolean isSosiaGear = sosiaIdList.containsKey(rs.getInt("sosia_id"));
                boolean isAdd = true;
                
                if (getGearCondition().equals(0)) {
                    // îÒòAìÆ
                    isAdd = !isSosiaGear;
                    
                } else if (getGearCondition().equals(1)) {
                    // òAìÆçœÇ›
                    isAdd = isSosiaGear;
                }
                
                if (isAdd) {
                    MobileMemberData mmd = new MobileMemberData();
                    mmd.setData(rs);
                    mmd.setSosiaGear(isSosiaGear);
                    mmd.setCustomerID((Integer)sosiaIdList.get(rs.getInt("sosia_id")));
                    this.add(mmd);
                }
            }
            
            rs.close();
        }
        catch(SQLException e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }

        return true;
    }
    
    public boolean loadCustomer(Integer sosiaID)
    {
        this.clear();

        try
        {
            ConnectionWrapper con = SystemInfo.getMobileConnection();

            ResultSetWrapper rs = con.executeQuery(this.getMobileMemberListSQL(sosiaID));

            if (rs.next()) {
                MobileMemberData mmd = new MobileMemberData();
                mmd.setData(rs);
                this.add(mmd);
            }
            
            rs.close();
        }
        catch(SQLException e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }

        return true;
    }
    
    private String getMobileMemberListSQL() {
        return this.getMobileMemberListSQL(null);
    }

    private String getMobileMemberListSQL(Integer sosiaID)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      a.add_date");
        sql.append("     ,b.cus_id as sosia_id");
        sql.append("     ,b.fm_name as cus_name1");
        sql.append("     ,b.fr_name as cus_name2");
        sql.append("     ,b.birth as birth_date");
//        sql.append("     ,b.email || (select name from name_list where name_id = 4 and seq = b.email_id) as email");
        sql.append("     ,b.email || b.email_domain as email");
        sql.append("     ,case when b.sex then 1 else 2 end as sex");
        //IVS_LVTu start add 2016/03/10 mmd.getSosiaID()
        sql.append("     ,b.fb_id");
        //IVS_LVTu end add 2016/03/10 mmd.getSosiaID()
        sql.append("     ,(select sosia_code from sosia.shop where "); 
        sql.append("     shop_id  = a.shop_id) as sosia_code "); 
        sql.append(" from");
        sql.append("     rel_cust_shop a");
        sql.append("         join customer b");
        sql.append("             using (cus_id)");
        sql.append(" where");
        
        //ëﬁâÔÇµÇƒÇ¢ÇÈâÔàıÇèúÇ≠
        sql.append("         a.state_flg <> 9");
        sql.append("     and b.state_flg <> 9");
        
        sql.append("     and a.shop_id in (select shop_id from sosia.shop where sosia_code in (" + sosiaCode + "))");
        
        if (addDateFrom != null) {
            sql.append(" and a.add_date >= " + SQLUtil.convertForSQL(addDateFrom));
        }

        if (addDateTo != null) {
            sql.append(" and a.add_date <= " + SQLUtil.convertForSQL(addDateTo));
        }

        if (sosiaID != null) {
            sql.append(" and b.cus_id = " + SQLUtil.convertForSQL(sosiaID));
        }
        if((sosiaIdSearch!= null && !sosiaIdSearch.equals(""))) {
            sql.append(" and b.cus_id like '%" + sosiaIdSearch+"%'");
        }
        if(firstNameSearch!= null && !firstNameSearch.equals("")) {
            sql.append(" and b.fm_name like '%" + firstNameSearch+"%'");
        }
        if(lastNameSearch!= null && !lastNameSearch.equals("")) {
            sql.append(" and b.fr_name like '%" + lastNameSearch+"%'");
        }
        sql.append(" order by");
        sql.append("      a.add_date");
        sql.append("     ,b.cus_id");
        
        return sql.toString();
    }

    private Map getGearList(ResultSetWrapper rsTmp) {
        
        Map result = new HashMap();
        
        try
        {
            ConnectionWrapper con = SystemInfo.getConnection();
            try {
                con.executeUpdate("drop table tmp_sosia_id_list;");
            } catch (Exception e) {}

            con.executeUpdate("create temporary table tmp_sosia_id_list(sosia_id integer)");

            boolean existsData = false;
            int i = 0;
            StringBuilder sosiaIdList = new StringBuilder(1000);
            while (rsTmp.next()) {
                existsData = true;
                if (sosiaIdList.length() > 0) sosiaIdList.append(" union all ");
                sosiaIdList.append("select " + rsTmp.getInt("sosia_id") + " as sosia_id ");
                i++;
                if (i > 1000) {
                    con.executeUpdate("insert into tmp_sosia_id_list " + sosiaIdList.toString());
                    sosiaIdList.setLength(0);
                    i = 0;
                }
            }
            if (sosiaIdList.length() > 0) {
                con.executeUpdate("insert into tmp_sosia_id_list " + sosiaIdList.toString());
            }

            if (existsData) {
                
                StringBuilder sql = new StringBuilder(1000);
                sql.append(" select");
                sql.append("      a.sosia_id");
                sql.append("     ,customer_id");
                sql.append(" from");
                sql.append("     mst_customer a");
                sql.append("        join tmp_sosia_id_list b using(sosia_id)");
                sql.append(" where");
                sql.append("         delete_date is null");
                sql.append(" order by");
                sql.append("      a.sosia_id");
                sql.append("     ,insert_date desc");
                sql.append("     ,update_date desc");
                
                ResultSetWrapper rs = con.executeQuery(sql.toString());

                while (rs.next()) {
                    if (result.containsKey(rs.getInt("sosia_id"))) continue;
                    result.put(rs.getInt("sosia_id"), rs.getInt("customer_id"));
                }

                rs.close();
            }
        }
        catch(SQLException e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }
    
}

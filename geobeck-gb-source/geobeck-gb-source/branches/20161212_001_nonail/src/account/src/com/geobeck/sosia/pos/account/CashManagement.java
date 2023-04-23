/*
 * CashManagement.java
 *
 * Created on 2013/04/07, 11:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.data.account.*;
import com.geobeck.sosia.pos.master.account.MstCashClass;
import com.geobeck.sosia.pos.master.account.MstCashMenu;

/**
 *
 * @author geobeck
 */
public class CashManagement extends ArrayList<DataCashManagement>
{
    private MstShop shop = new MstShop();
    private GregorianCalendar dateFrom = null;
    private GregorianCalendar dateTo = null;
    private Integer beforeTotal = 0;
    private Integer inTotal = 0;
    private Integer outTotal = 0;

    /**
     * Creates a new instance of CashManagement
     */
    public CashManagement() {
    }

    public MstShop getShop() {
        return shop;
    }

    public void setShop(MstShop shop) {
        this.shop = shop;
    }

    public GregorianCalendar getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(GregorianCalendar dateFrom) {
        this.dateFrom = dateFrom;
    }

    public GregorianCalendar getDateTo() {
        return dateTo;
    }

    public void setDateTo(GregorianCalendar dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getBeforeTotal() {
        return beforeTotal;
    }

    public void setBeforeTotal(Integer beforeTotal) {
        this.beforeTotal = beforeTotal;
    }

    public Integer getInTotal() {
        return inTotal;
    }

    public Integer getOutTotal() {
        return outTotal;
    }

    public Integer getBalanceTotal() {
        return beforeTotal + inTotal - outTotal;
    }

    public void load() {
        this.clear();
        inTotal = 0;
        outTotal = 0;

        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            // ŒJ‰z‹àŽæ“¾
            ResultSetWrapper rs = con.executeQuery(this.getBeforeTotalSQL());
            while (rs.next()) {
                this.beforeTotal = rs.getInt("before_total");
            }
            rs.close();

            rs = con.executeQuery(this.getLoadSQL());
            while (rs.next()) {
                DataCashManagement dcm = new DataCashManagement();
                dcm.setData(rs);
                MstStaff ms = new MstStaff();
                ms.setStaffID(rs.getInt("staff_id"));
                ms.setStaffNo(rs.getString("staff_no"));
                ms.setStaffName(0, rs.getString("staff_name1"));
                ms.setStaffName(1, rs.getString("staff_name2"));
                dcm.setStaff(ms);

                MstCashClass mcc = new MstCashClass();
                mcc.setCashClassId(rs.getInt("cash_class_id"));
                mcc.setCashClassName(rs.getString("cash_class_name"));
                dcm.setCashClass(mcc);

                MstCashMenu mcm = new MstCashMenu();
                mcm.setCashClass(mcc);
                mcm.setCashMenuId(rs.getInt("cash_menu_id"));
                mcm.setCashMenuName(rs.getString("cash_menu_name"));
                dcm.setCashMenu(mcm);

                if (dcm.isIn()) {
                    inTotal += dcm.getValue();
                } else {
                    outTotal += dcm.getValue();
                }

                this.add(dcm);
            }

            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private String getBeforeTotalSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select\n");
        sql.append("     coalesce(\n");
        sql.append("         sum(case when in_out then io_value else 0 end) -");
        sql.append("         sum(case when in_out then 0 else io_value end), 0) as before_total\n");
        sql.append(" from\n");
        sql.append("     data_cash_management\n");
        sql.append(" where\n");
        sql.append(" shop_id = ").append(SQLUtil.convertForSQL(this.getShop().getShopID())).append("\n");
        sql.append(" and management_date < ").append(SQLUtil.convertForSQLDateOnly(this.getDateFrom())).append("\n");
        //Start add 20131227 lvut ¬ŒûŒ»‹àŠÇ—‚ÌŒJ‰z‹àŠz‚É‚Â‚¢‚Ä
        sql.append(" and delete_date is null ");
        //End add 20131227 lvut ¬ŒûŒ»‹àŠÇ—‚ÌŒJ‰z‹àŠz‚É‚Â‚¢‚Ä
        return sql.toString();
    }

    private String getLoadSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      dcm.*");
        sql.append("     ,mst.staff_no");
        sql.append("     ,mst.staff_name1");
        sql.append("     ,mst.staff_name2");
        sql.append("     ,mcc.cash_class_name");
        sql.append("     ,mcm.cash_menu_name");
        sql.append(" from");
        sql.append("     data_cash_management dcm");
        sql.append("         left join mst_shop msp");
        sql.append("                on msp.shop_id = dcm.shop_id");
        sql.append("         left join mst_staff mst");
        sql.append("                on mst.staff_id = dcm.staff_id");
        sql.append("         left join mst_cash_class mcc");
        sql.append("                on mcc.cash_class_id = dcm.cash_class_id");
        sql.append("         left join mst_cash_menu mcm");
        sql.append("                on mcm.cash_class_id = dcm.cash_class_id");
        sql.append("               and mcm.cash_menu_id = dcm.cash_menu_id");
        sql.append(" where");
        sql.append("         dcm.delete_date is null");
        sql.append("     and dcm.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("     and dcm.management_date between " + SQLUtil.convertForSQLDateOnly(this.getDateFrom()));
        sql.append("                                 and " + SQLUtil.convertForSQLDateOnly(this.getDateTo()));
        sql.append(" order by");
        sql.append("      dcm.management_date desc");
        sql.append("     ,dcm.management_id desc");
        return sql.toString();
    }

}

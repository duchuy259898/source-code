/*
 * DataCashManagement.java
 *
 * Created on 2007/04/11, 11:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.account;

import com.geobeck.sosia.pos.master.account.MstCashClass;
import com.geobeck.sosia.pos.master.account.MstCashMenu;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author geobeck
 */
public class DataCashManagement
{
    private MstShop		shop		=	new MstShop();
    private Integer		managementId	=	null;
    private GregorianCalendar	managementDate	=	null;
    private boolean		in		=	true;
    private Integer		value		=	0;
    private String		evidenceNum	=	"";
    private MstCashClass        cashClass       =       new MstCashClass();
    private MstCashMenu         cashMenu        =       new MstCashMenu();
    private MstStaff		staff		=	new MstStaff();
    private String		useFor		=	"";
	
    /** Creates a new instance of DataCashManagement */
    public DataCashManagement() {
    }

    public String toString() {
        return this.getUseFor();
    }

    public MstShop getShop() {
        return shop;
    }

    public Integer getShopID() {
        if (shop == null) {
            return null;
        } else {
            return shop.getShopID();
        }
    }

    public void setShop(MstShop shop) {
        this.shop = shop;
    }

    public Integer getManagementId() {
        return managementId;
    }

    public void setManagementId(Integer managementId) {
        this.managementId = managementId;
    }

    public GregorianCalendar getManagementDate() {
        return managementDate;
    }

    public java.util.Date getManagementTime() {
            return managementDate.getTime();
    }

    public void setManagementDate(GregorianCalendar managementDate) {
        this.managementDate = managementDate;
    }

    public void setManagementDate(java.sql.Date managementDate) {
        this.managementDate = new GregorianCalendar();
        this.managementDate.setTime(managementDate);
    }

    public String getEvidenceNum() {
        return evidenceNum;
    }

    public void setEvidenceNum(String evidenceNum) {
        this.evidenceNum = evidenceNum;
    }

    public MstCashClass getCashClass() {
        return cashClass;
    }

    public Integer getCashClassId() {
        if (cashClass == null) {
            return null;
        }
        return cashClass.getCashClassId();
    }

    public String getCashClassName() {
        return cashClass.getCashClassName();
    }

    public void setCashClass(MstCashClass cashClass) {
        this.cashClass = cashClass;
    }

    public MstCashMenu getCashMenu() {
        return cashMenu;
    }

    public Integer getCashMenuId() {
        if (cashMenu == null) {
            return null;
        }
        return cashMenu.getCashMenuId();
    }

    public String getCashMenuName() {
        //Start edit 20140107 lvut ÉåÉWã‡ä«óùÅÀè¨å˚åªã‡ÅÀè¨å˚åªã‡ä«óù
        if (getCashMenuId() == 0 &&  getUseFor() !=null && getUseFor().indexOf("è¡âªPt") > 0) {
            return "∑¨Øº≠ ﬁØ∏";
        } else {
            return cashMenu.getCashMenuName();
        }
        //End edit 20140107 lvut ÉåÉWã‡ä«óùÅÀè¨å˚åªã‡ÅÀè¨å˚åªã‡ä«óù
    }

    public void setCashMenu(MstCashMenu cashMenu) {
        this.cashMenu = cashMenu;
    }

    public MstStaff getStaff() {
        return staff;
    }

    public String getStaffName() {
        return staff.toString();
    }
	
    public Integer getStaffID() {
        if (staff == null) {
            return null;
        } else {
            return staff.getStaffID();
        }
    }

    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }

    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getInValue() {
        return (in ? value : 0);
    }

    public Integer getOutValue() {
        return (!in ? value : 0);
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getUseFor() {
        return useFor;
    }

    public void setUseFor(String useFor) {
        this.useFor = useFor;
    }

    public void setData(DataCashManagement dcm) {
        MstShop shop = new MstShop();
        shop.setShopID(dcm.getShopID());
        this.setShop(shop);
        this.setManagementId(dcm.getManagementId());
        this.setManagementDate((GregorianCalendar) dcm.getManagementDate().clone());
        this.setIn(dcm.isIn());
        this.setValue(dcm.getValue());
        this.setEvidenceNum(dcm.getEvidenceNum());
        MstCashClass cashClass = new MstCashClass();
        cashClass.setCashClassId(dcm.getCashClassId());
        this.setCashClass(cashClass);
        MstCashMenu cashMenu = new MstCashMenu();
        cashMenu.setCashMenuId(dcm.getCashMenuId());
        this.setCashMenu(cashMenu);
        MstStaff staff = new MstStaff();
        staff.setStaffID(dcm.getStaffID());
        this.setStaff(staff);
        this.setUseFor(dcm.getUseFor());
    }

    public void setData(ResultSetWrapper rs) throws SQLException {
        MstShop msp = new MstShop();
        msp.setShopID(rs.getInt("shop_id"));
        this.setShop(msp);
        this.setManagementId(rs.getInt("management_id"));
        this.setManagementDate(rs.getDate("management_date"));
        this.setIn(rs.getBoolean("in_out"));
        this.setValue(rs.getInt("io_value"));
        this.setEvidenceNum(rs.getString("evidence_num"));
        MstCashClass cashClass = new MstCashClass();
        cashClass.setCashClassId(rs.getInt("cash_class_id"));
        this.setCashClass(cashClass);
        MstCashMenu cashMenu = new MstCashMenu();
        cashMenu.setCashMenuId(rs.getInt("cash_menu_id"));
        this.setCashMenu(cashMenu);
        MstStaff mst = new MstStaff();
        mst.setStaffID(rs.getInt("staff_id"));
        this.setStaff(mst);
        this.setUseFor(rs.getString("use_for"));
    }

    public boolean regist(ConnectionWrapper con) throws SQLException {
        if (this.getShopID() <= 0 || this.getManagementDate() == null || this.getValue() == 0) {
            return false;
        }

        if (this.isExist(con)) {
            if (con.executeUpdate(this.getUpdateSQL()) == 1) {
                return true;
            }
        } else {
            if (con.executeUpdate(this.getInsertSQL()) == 1) {
                this.setManagementId(this.getMaxManagementId(con));

                if (0 < this.getManagementId()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isExist(ConnectionWrapper con) throws SQLException {
        if (this.getShopID() == null || this.getManagementId() == null) {
            return false;
        }

        boolean result = false;

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            result = true;
        }

        rs.close();

        return result;
    }

    private Integer getMaxManagementId(ConnectionWrapper con) throws SQLException {
        if (this.getShopID() == null) {
            return 0;
        }

        Integer result = 0;

        ResultSetWrapper rs = con.executeQuery(this.getMaxManagementIdSQL());

        if (rs.next()) {
            result = rs.getInt("max_management_id");
        }

        rs.close();

        return result;
    }

    public boolean delete(ConnectionWrapper con) throws SQLException {
        if (this.getShopID() == null || this.getManagementId() == null) {
            return false;
        }

        return (con.executeUpdate(this.getDeleteSQL()) == 1);
    }

    private String getSelectSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      dcm.*");
        sql.append("     ,msp.group_id");
        sql.append("     ,msp.shop_name");
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
        sql.append("         dcm.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        sql.append("     and dcm.management_id = " + SQLUtil.convertForSQL(this.getManagementId()));
        return sql.toString();
    }

    private String getInsertSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" insert into data_cash_management (");
        sql.append("      shop_id");
        sql.append("     ,management_id");
        sql.append("     ,management_date");
        sql.append("     ,in_out");
        sql.append("     ,io_value");
        sql.append("     ,evidence_num");
        sql.append("     ,cash_class_id");
        sql.append("     ,cash_menu_id");
        sql.append("     ,staff_id");
        sql.append("     ,use_for");
        sql.append("     ,insert_date");
        sql.append("     ,update_date");
        sql.append("     ,delete_date");
        sql.append(" ) values (");
        sql.append("      " + SQLUtil.convertForSQL(this.getShopID()));
        sql.append("     ,(select coalesce(max(management_id), 0) + 1 from data_cash_management where shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + ")");
        sql.append("     ," + SQLUtil.convertForSQLDateOnly(this.getManagementDate()));
        sql.append("     ," + SQLUtil.convertForSQL(this.isIn()));
        sql.append("     ," + SQLUtil.convertForSQL(this.getValue()));
        sql.append("     ," + SQLUtil.convertForSQL(this.getEvidenceNum()));
        sql.append("     ," + SQLUtil.convertForSQL(this.getCashClassId()));
        sql.append("     ," + SQLUtil.convertForSQL(this.getCashMenuId()));
        sql.append("     ," + SQLUtil.convertForSQL(this.getStaffID()));
        sql.append("     ," + SQLUtil.convertForSQL(this.getUseFor()));
        sql.append("     ,current_timestamp");
        sql.append("     ,current_timestamp");
        sql.append("     ,null");
        sql.append(" );");
        return sql.toString();
    }

    private String getUpdateSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update data_cash_management");
        sql.append(" set");
        sql.append("      management_date = " + SQLUtil.convertForSQLDateOnly(this.getManagementDate()));
        sql.append("     ,in_out = " + SQLUtil.convertForSQL(this.isIn()));
        sql.append("     ,io_value = " + SQLUtil.convertForSQL(this.getValue()));
        sql.append("     ,evidence_num = " + SQLUtil.convertForSQL(this.getEvidenceNum()));
        sql.append("     ,cash_class_id = " + SQLUtil.convertForSQL(this.getCashClassId()));
        sql.append("     ,cash_menu_id = " + SQLUtil.convertForSQL(this.getCashMenuId()));
        sql.append("     ,staff_id = " + SQLUtil.convertForSQL(this.getStaffID()));
        sql.append("     ,use_for = " + SQLUtil.convertForSQL(this.getUseFor()));
        sql.append("     ,update_date = current_timestamp");
        sql.append("     ,delete_date = null");
        sql.append(" where");
        sql.append("         shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        sql.append("     and management_id = " + SQLUtil.convertForSQL(this.getManagementId()));
        return sql.toString();
    }

    private String getDeleteSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update data_cash_management");
        sql.append(" set");
        sql.append("      update_date = current_timestamp");
        sql.append("     ,delete_date = current_timestamp");
        sql.append(" where");
        sql.append("         shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        sql.append("     and management_id = " + SQLUtil.convertForSQL(this.getManagementId()));
        return sql.toString();
    }

    private String getMaxManagementIdSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     coalesce(max(management_id), 0) as max_management_id");
        sql.append(" from");
        sql.append("     data_cash_management");
        sql.append(" where");
        sql.append("     shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        return sql.toString();
    }
        
}

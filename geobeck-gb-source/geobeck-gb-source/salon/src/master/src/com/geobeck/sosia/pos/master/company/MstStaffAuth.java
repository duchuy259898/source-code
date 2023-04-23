/*
 * MstStaffAuth.java
 *
 * Created on 2007/12/13, 15:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * @author geobeck
 */
public class MstStaffAuth {

    private String      loginID     = "";
    private String      password    = "";
    private Boolean     ownerFlg    = false;
    private MstStaff    staff       = null;
    
    /**
     * コンストラクタ
     */
    public MstStaffAuth() {
    }

    /**
     * @return the loginID
     */
    public String getLoginID() {
        return loginID;
    }

    /**
     * @param loginID the loginID to set
     */
    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the ownerFlg
     */
    public Boolean getOwnerFlg() {
        return ownerFlg;
    }

    /**
     * @param ownerFlg the ownerFlg to set
     */
    public void setOwnerFlg(Boolean ownerFlg) {
        this.ownerFlg = ownerFlg;
    }

    /**
     * @return the staff
     */
    public MstStaff getStaff() {
        return staff;
    }

    /**
     * @param staff the staff to set
     */
    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }
    
    /**
     * データをクリアする。
     */
    public void clear() {
        this.setLoginID("");
        this.setPassword("");
        this.setOwnerFlg(false);
        this.setStaff(null);
    }
    
    public boolean load(ConnectionWrapper con) throws SQLException {

        if (con == null) return false;
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
        
        if(rs.next()) {
            this.setData(rs);
        }
        
        return true;
    }
    
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setLoginID(rs.getString("login_id"));
        this.setPassword(rs.getString("password"));
        this.setOwnerFlg(rs.getBoolean("owner_flg"));
        int staffID = rs.getInt("staff_id");
        if (staffID > 0) {
            MstStaff ms = new MstStaff(staffID);
            ms.load(SystemInfo.getConnection());
            this.setStaff(ms);
        }
    }

    public void setData(MstStaffAuth ms) {
        this.setLoginID(ms.getLoginID());
        this.setPassword(ms.getPassword());
        this.setOwnerFlg(ms.getOwnerFlg());
        this.setStaff(ms.getStaff());
    }
    
    public boolean regist(ConnectionWrapper con) throws SQLException {
        
        String sql = "";
        
        if (isExists(con)) {
            sql	= this.getUpdateSQL();
        } else {
            sql	= this.getInsertSQL();
        }
        
        if (con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        
        if (this.getLoginID() == null) return false;
        
        if (con == null) return false;
        
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
        
        return rs.next();
    }

    /**
     * Select文を取得する。
     * @return Select文
     */
    private String getSelectSQL() {
        return	"select *\n" +
                "from mst_staff_auth\n" +
                "where	login_id = " + SQLUtil.convertForSQL(this.getLoginID()) + "\n";
    }
    /**
     * Insert文を取得する。
     * @return Insert文
     */
    private String	getInsertSQL() {
        return	"insert into mst_staff_auth\n" +
                "(login_id, password, owner_flg, staff_id)\n" +
                "values (\n" +
                SQLUtil.convertForSQL(this.getLoginID()) + ",\n" +
                SQLUtil.convertForSQL(this.getPassword()) + ",\n" +
                SQLUtil.convertForSQL(this.getOwnerFlg()) + ",\n" +
                SQLUtil.convertForSQL(this.getOwnerFlg() ? null : this.getStaff().getStaffID()) + ")\n";
    }
    
    /**
     * Update文を取得する。
     * @return Update文
     */
    private String	getUpdateSQL() {
        return	"update mst_staff_auth\n" +
                "set\n" +
                "password = " + SQLUtil.convertForSQL(this.getPassword()) + ",\n" +
                "owner_flg = " + SQLUtil.convertForSQL(this.getOwnerFlg()) + ",\n" +
                "staff_id = " + SQLUtil.convertForSQL(this.getOwnerFlg() ? null : this.getStaff().getStaffID()) + "\n" +
                "where	login_id = " + SQLUtil.convertForSQL(this.getLoginID()) + "\n";
    }
    
    public boolean delete(ConnectionWrapper con) throws SQLException {
        
        String sql = "delete from mst_staff_auth where login_id = " + SQLUtil.convertForSQL(this.getLoginID());
        
        if(con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }
    
}

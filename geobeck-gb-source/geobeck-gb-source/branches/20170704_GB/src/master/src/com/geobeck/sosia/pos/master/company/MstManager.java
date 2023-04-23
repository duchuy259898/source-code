/*
 * MstManager.java
 *
 * Created on 2007/12/13, 15:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;



/**
 * 管理者マスタデータ
 * @author uchiyama
 */
public class MstManager {
    /**
     * 管理者ＩＤ
     */
    private	Integer			managerID			=	null;
    /**
     * ショップＩＤ
     */
    private	Integer			shopID			=	null;
    /**
     * 管理者名
     */
    private     String[]                managerName             =       {"", ""};
    /**
     * メールアドレス
     */
    private     String                  mailAddress              =       "";
    
    /** Creates a new instance of MstManager */
    
    /**
     * コンストラクタ
     */
    public MstManager() {
    }
    public MstManager(MstManager mm) {
        this.setData(mm);
    }
    
    /**
     * コンストラクタ
     * @param managerID 管理者ＩＤ
     */
    public MstManager(Integer managerID) {
        this.setManagerID(managerID);
    }
    
    /**
     * 文字列に変換する。（管理者名）
     * @return 管理者名
     */
    public String toString() {
        return	this.getManagerName(0) + "　" + this.getManagerName(1);
    }
    
    /**
     * 管理者ＩＤを取得する。
     * @return 管理者ＩＤ
     */
    public Integer getManagerID() {
        return managerID;
    }
    
    /**
     * 管理者ＩＤをセットする。
     * @param managerID 管理者ＩＤ
     */
    public void setManagerID(Integer managerID) {
        this.managerID = managerID;
    }
    
    /**
     * 管理者名を取得する。
     * @return 管理者名
     */
    public String[] getManagerName() {
        return managerName;
    }
    
    /**
     * 管理者名を取得する。
     * @param index インデックス
     * @return 管理者名
     */
    public String getManagerName(int index) {
        return managerName[index];
    }
    
    /**
     * 管理者名をセットする。
     * @param staffName 管理者名
     */
    public void setManagerName(String[] managerName) {
        this.managerName = managerName;
    }
    
    /**
     * 管理者名をセットする。
     * @param index インデックス
     * @param staffName 管理者名
     */
    public void setManagerName(int index, String managerName) {
        this.managerName[index] = managerName;
    }
    
    /**
     * 管理者のフルネームを取得する。
     * @return フルネーム
     */
    public String getFullManagerName() {
        return	(managerName[0] == null ? "" : managerName[0])
        +	(managerName[1] == null || managerName[1].equals("") ? "" : "　" + managerName[1]);
    }
    
    /**
     * メールアドレスを取得する。
     * @return メールアドレス
     */
    public String getMailAddress() {
        return mailAddress;
    }
    
    /**
     * メールアドレスをセットする。
     * @param mailAddress メールアドレス
     */
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }
    
    /**
     * 在籍店舗ＩＤをセットする。
     * @param shopID 在籍店舗ＩＤ
     */
    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }
    
    /**
     * 在籍店舗ＩＤを取得する。
     * @return 在籍店舗ＩＤ
     */
    public Integer getShopID() {
        return this.shopID;
    }
    
    /**
     * データをクリアする。
     */
    public void clear() {
        this.setManagerID(null);
        this.setManagerName(0, "");
        this.setManagerName(1, "");
        this.setMailAddress("");
        this.setShopID(null);
    }
    
    /**
     * 管理者マスタから、設定されている管理者IDのデータを読み込む。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean load(ConnectionWrapper con) throws SQLException {
        if(con == null)	return	false;
        
        ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
        
        if(rs.next()) {
            this.setData(rs);
        }
        
        return	true;
    }
    
    /**
     * MstManagerのデータを読み込む。
     * @param manager MstManager
     */
    public void setData( MstManager manager ) {
        this.setManagerID(manager.getManagerID() );
        this.setManagerName(0, manager.getManagerName(0) );
        this.setManagerName(1, manager.getManagerName(1) );
        this.setMailAddress(manager.getMailAddress() );
        this.setShopID(manager.getShopID() );
    }
    
    /**
     * ResultSetWrapperのデータを読み込む。
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setManagerID(rs.getInt("manager_id"));
        this.setManagerName(0, rs.getString("manager_name1"));
        this.setManagerName(1, rs.getString("manager_name2"));
        this.setMailAddress(rs.getString("mail_address"));
        this.setShopID(rs.getInt("shop_id"));
    }
    
    /**
     * 管理者マスタにデータを登録する。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
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
    
    /**
     * 管理者マスタにデータが存在するかチェックする。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        
        if (this.getManagerID() == null) return false;
        
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
                "from mst_manager\n" +
                "where	manager_id = " + SQLUtil.convertForSQL(this.getManagerID()) + "\n";
    }
    /**
     * Insert文を取得する。
     * @return Insert文
     */
    private String	getInsertSQL() {
        return	"insert into mst_manager\n" +
                "(manager_id, shop_id, manager_name1, manager_name2, mail_address,\n" +
                "insert_date, update_date, delete_date)\n" +
                "select\n" +
//---- 2013/04/24 GB MOD START
//                "coalesce(max(mst_manager.manager_id), 0) + 1,\n" +
                "coalesce(max(mm.manager_id), '0') + 1,\n" +
//---- 2013/04/24 GB MOD END
                SQLUtil.convertForSQL(this.getShopID()) + ",\n" +
                SQLUtil.convertForSQL(this.getManagerName(0)) + ",\n" +
                SQLUtil.convertForSQL(this.getManagerName(1)) + ",\n" +
                SQLUtil.convertForSQL(this.getMailAddress()) + ",\n" +  
                "current_timestamp, current_timestamp, null\n" +
                "from mst_manager mm";
    }
    
    /**
     * Update文を取得する。
     * @return Update文
     */
    private String	getUpdateSQL() {
        return	"update mst_manager\n" +
                "set\n" +
                "manager_name1 = " + SQLUtil.convertForSQL(this.getManagerName(0)) + ",\n" +
                "manager_name2 = " + SQLUtil.convertForSQL(this.getManagerName(1)) + ",\n" +
                "mail_address = " + SQLUtil.convertForSQL(this.getMailAddress()) + ",\n" +
                "shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + ",\n" +
                "update_date = current_timestamp\n" +
                "where	manager_id = " + SQLUtil.convertForSQL(this.getManagerID()) + "\n";
    }
    
    public boolean delete(ConnectionWrapper con) throws SQLException {
        
        String sql = "delete from mst_manager where manager_id = " + SQLUtil.convertForSQL(this.getManagerID());
        
        if(con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }
    
}

/*
 * MstTechnic.java
 *
 * Created on 2006/05/24, 11:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.master.product;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * 技術マスタデータ
 *
 * @author katagiri
 */
public class MstTechnic {

    /**
     * 技術分類
     */
    protected MstTechnicClass technicClass = new MstTechnicClass();
    /**
     * 技術ＩＤ
     */
    protected Integer technicID = null;
    /**
     * 技術コード
     */
    protected String technicNo = "";
    /**
     * 技術名
     */
    protected String technicName = "";
    /**
     * 単価
     */
    protected Long price = null;
    /**
     * 施術時間
     */
    protected Integer operationTime = null;
    /**
     * 表示順
     */
    protected Integer displaySeq = null;
    protected Integer status = null;
    /**
     * モバイル予約用技術No(変更不可)
     */
    protected String mobileTechnicNo = new String("mo-rsv-");
    protected Integer praiseTimeLimit = new Integer(0);
    protected Boolean isPraiseTime = false;
    protected Integer mobileFlag = 0;

    public Integer getMobileFlag() {
        return mobileFlag;
    }

    public void setMobileFlag(Integer mobileFlag) {
        this.mobileFlag = mobileFlag;
    }

   

    /**
     * Creates a new instance of MstTechnic
     */
    public MstTechnic() {
    }

    /**
     * 技術分類を取得する。
     *
     * @return 技術分類
     */
    public MstTechnicClass getTechnicClass() {
        return technicClass;
    }

    /**
     * 技術分類をセットする。
     *
     * @param technicClass 技術分類
     */
    public void setTechnicClass(MstTechnicClass technicClass) {
        this.technicClass = technicClass;
    }

    /**
     * 技術ＩＤを取得する。
     *
     * @return 技術ＩＤ
     */
    public Integer getTechnicID() {
        return technicID;
    }

    /**
     * 技術ＩＤをセットする。
     *
     * @param technicID 技術ＩＤ
     */
    public void setTechnicID(Integer technicID) {
        this.technicID = technicID;
    }

    /**
     * 技術コードを取得する。
     *
     * @return 技術コード
     */
    public String getTechnicNo() {
        return technicNo;
    }

    /**
     * 技術コードをセットする。
     *
     * @param technicNo 技術コード
     */
    public void setTechnicNo(String technicNo) {
        this.technicNo = technicNo;
    }

    /**
     * 技術名を取得する。
     *
     * @return 技術名
     */
    public String getTechnicName() {
        return technicName;
    }

    /**
     * 技術名をセットする。
     *
     * @param technicName 技術名
     */
    public void setTechnicName(String technicName) {
        this.technicName = technicName;
    }

    /**
     * 単価を取得する。
     *
     * @return 単価
     */
    public Long getPrice() {
        return price;
    }

    /**
     * 単価をセットする。
     *
     * @param price 単価
     */
    public void setPrice(Long price) {
        this.price = price;
    }

    /**
     * 施術時間を取得する。
     *
     * @return 施術時間
     */
    public Integer getOperationTime() {
        return operationTime;
    }

    /**
     * 施術時間をセットする。
     *
     * @param operationTime 施術時間
     */
    public void setOperationTime(Integer operationTime) {
        this.operationTime = operationTime;
    }

    /**
     * 表示順を取得する。
     *
     * @return 表示順
     */
    public Integer getDisplaySeq() {
        return displaySeq;
    }

    /**
     * 表示順をセットする。
     *
     * @param displaySeq 表示順
     */
    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String toString() {
        return this.getTechnicName();
    }

    /**
     * モバイル予約用　技術Noをセットする。
     */
    public void setMobileTechnicNo(String mobileTechnicNo) {
        this.mobileTechnicNo = mobileTechnicNo;
    }

    /**
     * モバイル予約用　技術Noを取得する。
     *
     * @param mobileTechnicNo
     */
    public String getMobileTechnicNo() {
        return mobileTechnicNo;
    }

    public void setPraiseTimeLimit(Integer praiseTime) {
        this.praiseTimeLimit = praiseTime;
    }

    public Integer getPraiseTimeLimit() {
        return this.praiseTimeLimit;
    }

    public void setPraiseTime(Boolean b) {
        this.isPraiseTime = b;
    }

    public Boolean isPraiseTime() {
        return this.isPraiseTime;
    }

    /**
     * 技術マスタデータからデータをセットする。
     *
     * @param mt 技術マスタデータ
     */
    public void setData(MstTechnic mt) {
        this.setTechnicClass(new MstTechnicClass(mt.getTechnicClass().getTechnicClassID()));
        this.setTechnicID(mt.getTechnicID());
        this.setTechnicNo(mt.getTechnicNo());
        this.setTechnicName(mt.getTechnicName());
        this.setPrice(mt.getPrice());
        this.setOperationTime(mt.getOperationTime());
        this.setDisplaySeq(mt.getDisplaySeq());
        this.setPraiseTimeLimit(mt.getPraiseTimeLimit());
        this.setPraiseTime(mt.isPraiseTime());
        //this.setStatus(mt.getStatus());
        this.setMobileFlag(mt.getMobileFlag());
    }

    /**
     * ResultSetWrapperからデータをセットする
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setTechnicClass(new MstTechnicClass(rs.getInt("technic_class_id")));
        this.setTechnicID(rs.getInt("technic_id"));
        this.setTechnicNo(rs.getString("technic_no"));
        this.setTechnicName(rs.getString("technic_name"));
        this.setPrice(rs.getLong("price"));
        this.setOperationTime(rs.getInt("operation_time"));
        this.setDisplaySeq(rs.getInt("display_seq"));
        this.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
        this.setPraiseTime(rs.getBoolean("is_praise_time"));
        this.setMobileFlag(rs.getInt("mobile_flag"));
        
        //this.setStatus(rs.getInt("status"));
    }

    /**
     * 技術マスタにデータを登録する。
     *
     * @return true - 成功
     * @param lastSeq 表示順の最大値
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public boolean regist(ConnectionWrapper con,
            Integer lastSeq) throws SQLException {
        if (isExists(con)) {
            if (lastSeq != this.getDisplaySeq()) {
                if (0 < lastSeq) {
                    if (con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0) {
                        return false;
                    }
                }

                if (0 <= this.getDisplaySeq()) {
                    if (con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0) {
                        return false;
                    }
                }
            }

            if (con.executeUpdate(this.getUpdateSQL()) != 1) {
                return false;
            }
        } else {
            if (0 <= this.getDisplaySeq()) {
                if (con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0) {
                    return false;
                }
            }

            if (con.executeUpdate(this.getInsertSQL()) != 1) {
                return false;
            }

            this.setMaxTechnicID(con);
        }

        return true;
    }

    private void setMaxTechnicID(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(getMaxTechnicIDSQL());

        if (rs.next()) {
            
            this.setTechnicID(rs.getInt("max_id"));
        }

        rs.close();
    }

    /**
     * 技術マスタからデータを削除する。（論理削除）
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean delete(ConnectionWrapper con) throws SQLException {
        String sql = "";

        if (isExists(con)) {
            sql = this.getDeleteSQL();
        } else {
            return false;
        }

        if (con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0) {
            return false;
        }

        if (con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 技術マスタにデータが存在するかチェックする。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getTechnicID() == null) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }
    
    //IVS_LVTu start add 2016/05/27 New request #50223
    public boolean loadByTechnicID(ConnectionWrapper con) throws SQLException {
        if (this.getTechnicID()== null) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            this.setData(rs);
            rs.close();
            return true;
        }

        rs.close();
        return false;
    }
    //IVS_LVTu end add 2016/05/27 New request #50223

    /**
     * 技術マスタにデータが存在するかチェックする。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean loadByTechnicNo(ConnectionWrapper con) throws SQLException {
        if (this.getTechnicNo() == null || this.getTechnicNo().equals("")) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectByTechnicNoSQL());

        if (rs.next()) {
            this.setData(rs);
            rs.close();
            return true;
        }

        rs.close();
        return false;
    }

    /**
     * 技術マスタにデータが存在するかチェックする。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExistsByTechnicNo(ConnectionWrapper con) throws SQLException {
        if (this.getTechnicNo() == null || this.getTechnicNo().equals("")) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectByTechnicNoSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectSQL() {
        return "select *\n"
                + "from mst_technic\n"
                + "where	technic_id = " + SQLUtil.convertForSQL(this.getTechnicID()) + "\n";
    }

    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectByTechnicNoSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      mtc.technic_class_name");
        sql.append("     ,mt.*");
        sql.append(" from");
        sql.append("     mst_technic mt");
        sql.append("         join mst_technic_class mtc");
        sql.append("         using (technic_class_id)");
        sql.append(" where");
        sql.append("         mt.delete_date is null");
        sql.append("     and mtc.delete_date is null");
        sql.append("     and technic_no = " + SQLUtil.convertForSQL(this.getTechnicNo()));

        return sql.toString();
    }

    /**
     * 表示順をずらすＳＱＬ文を取得する
     *
     * @param seq 元の表示順
     * @param isIncrement true - 加算、false - 減算
     * @return 表示順をずらすＳＱＬ文
     */
    private String getSlideSQL(Integer seq, boolean isIncrement) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update mst_technic");
        sql.append(" set");
        sql.append("     display_seq = display_seq " + (isIncrement ? "+" : "-") + " 1");
        sql.append(" where");
        sql.append("         delete_date is null");
        sql.append("     and technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()));
        sql.append("     and display_seq >= " + SQLUtil.convertForSQL(seq));
        sql.append("     and display_seq " + (isIncrement ? "+" : "-") + " 1 >= 0");

        if (!isIncrement) {
            sql.append("     and not exists");
            sql.append("            (");
            sql.append("                 select 1");
            sql.append("                 from");
            sql.append("                     (");
            sql.append("                         select");
            sql.append("                             count(*) as cnt");
            sql.append("                         from");
            sql.append("                             mst_technic");
            sql.append("                         where");
            sql.append("                                 technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()));
            sql.append("                             and delete_date is null");
            sql.append("                         group by");
            sql.append("                             display_seq");
            sql.append("                     ) t");
            sql.append("                 where");
            sql.append("                     cnt > 1");
            sql.append("            )");
        }

        return sql.toString();
    }

    /**
     * Insert文を取得する。
     *
     * @return Insert文
     */
    private String getInsertSQL() {
        return "insert into mst_technic\n"
                + "(technic_class_id, technic_id, technic_no, technic_name,\n"
                + "price, operation_time, display_seq, praise_time_limit, is_praise_time, \n"
                + "mobile_flag,insert_date, update_date, delete_date)\n"
                + "select\n"
                + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()) + ",\n"
                + "(select coalesce(max(technic_id), 0) + 1\n"
                //IVS_LVTu start edit 2016/12/13 New request #58893
                + "from mst_technic\n"
                + "where technic_id > 0),\n"
                //IVS_LVTu end edit 2016/12/13 New request #58893
                + SQLUtil.convertForSQL(this.getTechnicNo()) + ",\n"
                + SQLUtil.convertForSQL(this.getTechnicName()) + ",\n"
                + SQLUtil.convertForSQL(this.getPrice()) + ",\n"
                + SQLUtil.convertForSQL(this.getOperationTime()) + ",\n"
                + "case\n"
                + "when " + SQLUtil.convertForSQL(this.getDisplaySeq())
                + " between 0 and coalesce((select max(display_seq)\n"
                + "from mst_technic\n"
                + "where delete_date is null\n"
                + "and technic_class_id = "
                + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()) + "), 0) then "
                + SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from mst_technic\n"
                + "where delete_date is null\n"
                + "and technic_class_id = "
                + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()) + "), 0) + 1 end,\n"
                + SQLUtil.convertForSQL(this.getPraiseTimeLimit()) + ",\n"
                + SQLUtil.convertForSQL(this.isPraiseTime()) + ",\n"
                + SQLUtil.convertForSQL(this.getMobileFlag()) + ",\n"
                + "current_timestamp, current_timestamp, null\n";
    }

    /**
     * Update文を取得する。
     *
     * @return Update文
     */
    private String getUpdateSQL() {
        return "update mst_technic\n"
                + "set\n"
                + "technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()) + ",\n"
                + "technic_no = " + SQLUtil.convertForSQL(this.getTechnicNo()) + ",\n"
                + "technic_name = " + SQLUtil.convertForSQL(this.getTechnicName()) + ",\n"
                + "price = " + SQLUtil.convertForSQL(this.getPrice()) + ",\n"
                + "operation_time = " + SQLUtil.convertForSQL(this.getOperationTime()) + ",\n"
                + "praise_time_limit = " + SQLUtil.convertForSQL(this.getPraiseTimeLimit()) + ",\n"
                + "is_praise_time = " + SQLUtil.convertForSQL(this.isPraiseTime()) + ",\n"
                + "mobile_flag = " + SQLUtil.convertForSQL(this.getMobileFlag()) + ",\n"
                + "display_seq = case\n"
                + "when " + SQLUtil.convertForSQL(this.getDisplaySeq())
                + " between 0 and coalesce((select max(display_seq)\n"
                + "from mst_technic\n"
                + "where delete_date is null\n"
                + "and technic_class_id = "
                + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()) + "\n"
                + "and technic_id != "
                + SQLUtil.convertForSQL(this.getTechnicID()) + "), 0) then "
                + SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from mst_technic\n"
                + "where delete_date is null\n"
                + "and technic_class_id = "
                + SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID()) + "\n"
                + "and technic_id != "
                + SQLUtil.convertForSQL(this.getTechnicID()) + "), 0) + 1 end,\n"
                + "update_date = current_timestamp\n"
                + "where	technic_id = " + SQLUtil.convertForSQL(this.getTechnicID()) + "\n";
    }

    /**
     * 削除用Update文を取得する。
     *
     * @return 削除用Update文
     */
    private String getDeleteSQL() {
        return "update mst_technic\n"
                + "set\n"
                + "delete_date = current_timestamp\n"
                + "where	technic_id = " + SQLUtil.convertForSQL(this.getTechnicID()) + "\n";
    }

    private static String getMaxTechnicIDSQL() {
        return "select max(technic_id) as max_id\n"
                + "from mst_technic\n";
    }

    //マスタ一括登録 add start 2017/01/07
    /**
     * 一括登録用の登録処理を実行
     * @param con コネクション
     * @return true:正常 / false:異常
     * @throws SQLException 
     */
    public boolean registForBulk(ConnectionWrapper con) throws SQLException {
        
        if (con.executeUpdate(this.getInsertSqlForBulk()) != 1) {
            return false;
        }

        //技術IDを自動採番のため取得して保持
        this.setMaxTechnicID(con);

        return true;
    }

    /**
     * 一括登録用SQLを返す
     * @return SQL文
     */
    private String getInsertSqlForBulk() {
        
        StringBuilder sql = new StringBuilder();
        
        sql.append("insert into mst_technic\n");
        sql.append("(\n");
        sql.append("technic_class_id,\n");
        sql.append("technic_id,\n");
        sql.append("technic_no,\n");
        sql.append("technic_name,\n");
        sql.append("price,\n");
        sql.append("operation_time,\n");
        sql.append("display_seq,\n");
        sql.append("insert_date,\n");
        sql.append("update_date,\n");
        sql.append("delete_date,\n");
        sql.append("praise_time_limit,\n");
        sql.append("is_praise_time,\n");
        sql.append("sex_flg,\n");
        sql.append("credit_flg,\n");
        sql.append("mobile_flag \n");
        sql.append(") values(\n");
        sql.append(SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID())).append(",\n");
        sql.append("(select coalesce(max(technic_id), 0) + 1 from mst_technic), \n");
        sql.append(SQLUtil.convertForSQL(this.getTechnicNo())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getTechnicName())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getPrice())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getOperationTime())).append(",\n");
        sql.append("(select coalesce(max(display_seq), 0) + 1 from mst_technic where technic_class_id =");
        sql.append(SQLUtil.convertForSQL(this.getTechnicClass().getTechnicClassID())).append(" and delete_date is null), \n");
        sql.append("current_timestamp,\n");
        sql.append("current_timestamp,\n");
        sql.append("null,\n");
        sql.append(SQLUtil.convertForSQL(this.getPraiseTimeLimit())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.isPraiseTime())).append(",\n");
        sql.append("0,\n");
        sql.append(SQLUtil.convertForSQL(Boolean.TRUE)).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getMobileFlag())).append(" \n");
        sql.append(")");

        return sql.toString();
    }
    //マスタ一括登録 add end 2017/01/07
}

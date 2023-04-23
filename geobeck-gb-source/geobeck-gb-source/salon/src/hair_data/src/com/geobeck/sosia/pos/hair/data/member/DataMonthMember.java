/*
 * DataMonthMember.java
 *
 * Created on 2018/03/05, 9:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.member;

import com.geobeck.sosia.pos.hair.master.product.MstPlan;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import java.sql.*;
import java.util.*;
import java.util.Date;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * 月会員データ
 *
 * @author lvtu
 */
public class DataMonthMember {

    /**
     * 入会店舗
     */
    protected MstShop mShop = null;
    /**
     * 月契約ID
     */
    protected Integer monthContractID = null;
    /**
     * 顧客
     */
    protected MstCustomer mCustomer = null;
    /**
     * プラン
     */
    protected MstPlan mPlan = null;
    /**
     * 入会日
     */
    protected Date joinDate = null;
    /**
     * 停止日
     */
    protected Date stopDate = null;
    /**
     * 退会日
     */
    protected Date withdrawalDate = null;
    /**
     * ステータス
     */
    protected Integer status = null;
    /**
     * メモ
     */
    protected String memo = "";
    
    /**
     * ステータス
     */
    protected String statusList = "";

    /**
     * 入会日From
     */
    protected Date joinDateFrom = null;
    /**
     * 停止日From
     */
    protected Date stopDateFrom = null;
    /**
     * 退会日From
     */
    protected Date withdrawalDateFrom = null;
    
    /**
     * 入会日To
     */
    protected Date joinDateTo = null;
    /**
     * 停止日To
     */
    protected Date stopDateTo = null;
    /**
     * 退会日To
     */
    protected Date withdrawalDateTo = null;

    /**
     * 入会店舗を取得する。
     *
     * @return 入会店舗
     */
    public MstShop getmShop() {
        return mShop;
    }

    /**
     * 入会店舗をセットする。
     *
     * @param mShop 入会店舗
     */
    public void setmShop(MstShop mShop) {
        this.mShop = mShop;
    }

    /**
     * 月契約IDを取得する。
     *
     * @return 月契約ID
     */
    public Integer getMonthContractID() {
        return monthContractID;
    }

    /**
     * 月契約IDをセットする。
     *
     * @param monthContractID 月契約ID
     */
    public void setMonthContractID(Integer monthContractID) {
        this.monthContractID = monthContractID;
    }

    /**
     * 顧客を取得する。
     *
     * @return 顧客
     */
    public MstCustomer getmCustomer() {
        return mCustomer;
    }

    /**
     * 顧客をセットする。
     *
     * @param mCustomer 顧客
     */
    public void setmCustomer(MstCustomer mCustomer) {
        this.mCustomer = mCustomer;
    }

    /**
     * プランを取得する。
     *
     * @return プラン
     */
    public MstPlan getmPlan() {
        return mPlan;
    }

    /**
     * プランをセットする。
     *
     * @param mPlan プラン
     */
    public void setmPlan(MstPlan mPlan) {
        this.mPlan = mPlan;
    }

    /**
     * 入会日を取得する。
     *
     * @return 入会日
     */
    public Date getJoinDate() {
        return joinDate;
    }

    /**
     * 入会日をセットする。
     *
     * @param joinDate 入会日
     */
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    /**
     * 入会店舗を取得する。
     *
     * @return 入会店舗
     */
    public Date getStopDate() {
        return stopDate;
    }

    /**
     * 停止日をセットする。
     *
     * @param stopDate 停止日
     */
    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    /**
     * 退会日を取得する。
     *
     * @return 退会日
     */
    public Date getWithdrawalDate() {
        return withdrawalDate;
    }

    /**
     * 退会日をセットする。
     *
     * @param withdrawalDate 退会日
     */
    public void setWithdrawalDate(Date withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    /**
     * ステータスを取得する。
     *
     * @return ステータス
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * ステータスをセットする。
     *
     * @param status ステータス
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * メモを取得する。
     *
     * @return メモ
     */
    public String getMemo() {
        return memo;
    }

    /**
     * メモをセットする。
     *
     * @param memo 商品ＩＤ
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }
    
    public String getStatusList() {
        return statusList;
    }

    public void setStatusList(String statusList) {
        this.statusList = statusList;
    }
    
    public Date getJoinDateFrom() {
        return joinDateFrom;
    }

    public void setJoinDateFrom(Date joinDateFrom) {
        this.joinDateFrom = joinDateFrom;
    }

    public Date getStopDateFrom() {
        return stopDateFrom;
    }

    public void setStopDateFrom(Date stopDateFrom) {
        this.stopDateFrom = stopDateFrom;
    }

    public Date getWithdrawalDateFrom() {
        return withdrawalDateFrom;
    }

    public void setWithdrawalDateFrom(Date withdrawalDateFrom) {
        this.withdrawalDateFrom = withdrawalDateFrom;
    }

    public Date getJoinDateTo() {
        return joinDateTo;
    }

    public void setJoinDateTo(Date joinDateTo) {
        this.joinDateTo = joinDateTo;
    }

    public Date getStopDateTo() {
        return stopDateTo;
    }

    public void setStopDateTo(Date stopDateTo) {
        this.stopDateTo = stopDateTo;
    }

    public Date getWithdrawalDateTo() {
        return withdrawalDateTo;
    }

    public void setWithdrawalDateTo(Date withdrawalDateTo) {
        this.withdrawalDateTo = withdrawalDateTo;
    }
    
    @Override
    public String toString() {
        return (this.getmShop()!= null) ? this.getmShop().getShopName() : "";
    }

    /**
     * コンストラクタ
     */
    public DataMonthMember() {
    }

    /**
     * 月会員データからデータをセットする。
     *
     * @param data 月会員データ
     */
    public void setData(DataMonthMember data) {
        this.setmShop(data.getmShop());
        this.setMonthContractID(data.getMonthContractID());
        this.setmCustomer(data.getmCustomer());
        this.setmPlan(data.getmPlan());
        this.setJoinDate(data.getJoinDate());
        this.setStopDate(data.getStopDate());
        this.setWithdrawalDate(data.getWithdrawalDate());
        this.setStatus(data.getStatus());
        this.setMemo(data.getMemo());
    }

    /**
     * ResultSetWrapperからデータをセットする
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        MstShop shop = new MstShop();
        shop.setShopID(rs.getInt("shop_id"));
        shop.setShopName(rs.getString("shop_name"));
        this.setmShop(shop);
        this.setMonthContractID(rs.getInt("month_contract_id"));
        MstCustomer customer = new MstCustomer();
        customer.setCustomerID(rs.getInt("customer_id"));
        customer.setCustomerNo(rs.getString("customer_no"));
        customer.setCustomerName(new String[]{rs.getString("customer_name1"), rs.getString("customer_name2")});
        this.setmCustomer(customer);
        MstPlan plan = new MstPlan();
        plan.setPlanId(rs.getInt("plan_id"));
        plan.setPlanName(rs.getString("plan_name"));
        this.setmPlan(plan);
        this.setJoinDate(rs.getDate("join_date"));
        this.setStopDate(rs.getDate("stop_date"));
        this.setWithdrawalDate(rs.getDate("withdrawal_date"));
        this.setStatus(rs.getInt("status"));
        this.setMemo(rs.getString("memo"));
    }

    /**
     * 月会員データを登録する。
     *
     * @return true - 成功
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        if (isExists(con)) {

            if (con.executeUpdate(this.getUpdateSQL()) != 1) {
                return false;
            }
        } else if (con.executeUpdate(this.getInsertSQL()) != 1) {
            return false;
        }

        return true;
    }

    /**
     * 月会員データを削除する。（論理削除）
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

        if (con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 月会員データが存在するかチェックする。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getMonthContractID() == null) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectDataSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 月会員データ
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 取得成功する
     */
    public ArrayList<DataMonthMember> load(ConnectionWrapper con) throws SQLException {

        ArrayList<DataMonthMember> listMonthMember = new ArrayList<DataMonthMember>();
        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        while (rs.next()) {
            DataMonthMember datamm = new DataMonthMember();
            datamm.setData(rs);
            
            listMonthMember.add(datamm);
        }

        rs.close();
        return listMonthMember;
    }

    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectSQL() {
        String sql = "select mm.*\n"
                + ",shop.shop_id\n"
                + ",shop.shop_name\n"
                + ",customer.customer_id\n"
                + ",customer.customer_no\n"
                + ",customer.customer_name1\n"
                + ",customer.customer_name2\n"
                + ",plan.plan_id\n"
                + ",plan.plan_name\n"
                + "from data_month_member mm\n"
                + "inner join mst_shop shop\n"
                + "on mm.shop_id = shop.shop_id\n"
                + "and shop.delete_date is null\n"
                + "inner join mst_customer customer\n"
                + "on mm.customer_id = customer.customer_id\n"
                + "and customer.delete_date is null\n"
                + "inner join mst_plan plan\n"
                + "on mm.plan_id = plan.plan_id\n"
                + "where mm.delete_date is null\n";
                if(this.getmShop() != null && this.getmShop().getShopID() != null) {
                   sql = sql + "and mm.shop_id in ("+ SQLUtil.convertForSQL(this.getmShop().getShopID()) +")\n";
                }
                if(this.getmCustomer()!= null && this.getmCustomer().getCustomerID()!= null) {
                   sql = sql + "and mm.customer_id = "+ SQLUtil.convertForSQL(this.getmCustomer().getCustomerID()) +"\n";
                }
                if(this.getmPlan()!= null && this.getmPlan().getPlanId() != null) {
                   sql = sql + "and mm.plan_id = "+ SQLUtil.convertForSQL(this.getmPlan().getPlanId()) +"\n";
                }
                if(!this.getStatusList().equals("")) {
                   sql = sql + "and mm.status in ("+ this.getStatusList() +")\n";
                }
                if(!this.getMemo().equals("")) {
                   sql = sql + "and mm.memo like '%"+ this.getMemo() +"%'\n";
                }
                if(this.getJoinDateFrom() != null || this.getJoinDateTo() != null) {
                    sql = sql + this.sqlWhereDate("mm.join_date", this.getJoinDateFrom(), this.getJoinDateTo());
                }
                if(this.getStopDateFrom() != null || this.getStopDateTo() != null) {
                    sql = sql + this.sqlWhereDate("mm.stop_date", this.getStopDateFrom(), this.getStopDateTo());
                }
                if(this.getWithdrawalDateFrom() != null || this.getWithdrawalDateTo() != null) {
                    sql = sql + this.sqlWhereDate("mm.withdrawal_date", this.getWithdrawalDateFrom(), this.getWithdrawalDateTo());
                }
                sql = sql + "order by mm.shop_id, mm.month_contract_id\n";

        return sql;
    }
    
    
    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectDataSQL() {
        return "select *\n"
                + "from data_month_member\n"
                + "where month_contract_id = " + SQLUtil.convertForSQL(this.getMonthContractID()) + "\n";
    }
    
    /**
     * @param columnName 
     * @param from
     * @param to
     * @return String
     */
    private String sqlWhereDate(String columnName, Date from, Date to) {
        String sqlWhere = "";
        if(from != null && to != null) {
            sqlWhere =  "and " + columnName + " between "+ SQLUtil.convertForSQL(from) +" and "+ SQLUtil.convertForSQL(to) +"\n";
        }else if(from != null) {
            sqlWhere =  "and " + columnName + " >= "+ SQLUtil.convertForSQL(from) +"\n";
        }else if(to != null) {
            sqlWhere =  "and " + columnName + " <= "+ SQLUtil.convertForSQL(to) +"\n";
        }
        
        return sqlWhere;
    }
    

    /**
     * Insert文を取得する。
     *
     * @return Insert文
     */
    private String getInsertSQL() {
        return "insert into data_month_member\n"
                + "(shop_id, \n"
                + "month_contract_id, \n"
                + "customer_id, \n"
                + "plan_id, \n"
                + "join_date,\n"
                + "stop_date,\n"
                + "withdrawal_date,\n"
                + "status, memo,\n"
                + "insert_date, update_date, delete_date)\n"
                + "select\n"
                + SQLUtil.convertForSQL(this.getmShop().getShopID()) + ",\n"
                + "(select coalesce(max(month_contract_id), 0) + 1\n"
                + "from data_month_member),\n"
                + SQLUtil.convertForSQL(this.getmCustomer().getCustomerID()) + ",\n"
                + SQLUtil.convertForSQL(this.getmPlan().getPlanId()) + ",\n"
                + SQLUtil.convertForSQL(this.getJoinDate()) + ",\n"
                + "null,\n"
                + "null,\n"
                + SQLUtil.convertForSQL(this.getStatus()) + ",\n"
                + SQLUtil.convertForSQL(this.getMemo()) + ",\n"
                + "current_timestamp, current_timestamp, null\n";
    }

    /**
     * Update文を取得する。
     *
     * @return Update文
     */
    private String getUpdateSQL() {
        return "update data_month_member\n"
                + "set\n"
                + "shop_id =" + SQLUtil.convertForSQL(this.getmShop().getShopID()) + ", \n"
                + "customer_id =" + SQLUtil.convertForSQL(this.getmCustomer().getCustomerID()) + ", \n"
                + "plan_id =" + SQLUtil.convertForSQL(this.getmPlan().getPlanId()) + ", \n"
                + "join_date =" + SQLUtil.convertForSQL(this.getJoinDate()) + ",\n"
                + "stop_date =" + SQLUtil.convertForSQL(this.getStopDate()) + ",\n"
                + "withdrawal_date =" + SQLUtil.convertForSQL(this.getWithdrawalDate()) + ",\n"
                + "status =" + SQLUtil.convertForSQL(this.getStatus()) + ",\n"
                + "memo =" + SQLUtil.convertForSQL(this.getMemo()) + ",\n"
                + "update_date = current_timestamp\n"
                + "where month_contract_id = " + SQLUtil.convertForSQL(this.getMonthContractID()) + "\n";
    }

    /**
     * 削除用Update文を取得する。
     *
     * @return 削除用Update文
     */
    private String getDeleteSQL() {
        return "update data_month_member\n"
                + "set\n"
                + "delete_date = current_timestamp\n"
                + "where month_contract_id = " + SQLUtil.convertForSQL(this.getMonthContractID()) + "\n";
    }
}

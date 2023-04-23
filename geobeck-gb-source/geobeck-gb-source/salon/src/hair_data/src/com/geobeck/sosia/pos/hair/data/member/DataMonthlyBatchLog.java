/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.member;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * 一括処理ログ
 *
 * @author lvtu
 */
public class DataMonthlyBatchLog extends ArrayList<DataMonthlyBatchDetailLog> {

    /**
     * 処理ID
     */
    protected Integer batchID = null;
    /**
     * 店舗
     */
    protected MstShop shop = null;
    /**
     * 対象月
     */
    protected Date targetMonth = null;
    /**
     * 処理日
     */
    protected Date processDate = null;
    /**
     * レジ担当者
     */
    protected MstStaff staff = null;
    /**
     * 支払方法
     */
    protected Integer paymentMethodID = null;

    /**
     * 未処理
     */
    protected boolean processed = false;

    /**
     * 処理済み
     */
    protected boolean untreated = false;

    /**
     * 処理IDを取得する。
     *
     * @return 処理ID
     */
    public Integer getBatchID() {
        return batchID;
    }

    /**
     * 処理IDをセットする。
     *
     * @param batchID
     */
    public void setBatchID(Integer batchID) {
        this.batchID = batchID;
    }

    /**
     * 店舗を取得する。
     *
     * @return 店舗
     */
    public MstShop getShop() {
        return shop;
    }

    /**
     * 入会店舗をセットする。
     *
     * @param shop 入会店舗
     */
    public void setShop(MstShop shop) {
        this.shop = shop;
    }

    /**
     * 対象月を取得する。
     *
     * @return 対象月
     */
    public Date getTargetMonth() {
        return targetMonth;
    }

    /**
     * 対象月をセットする。
     *
     * @param targetMonth 対象月
     */
    public void setTargetMonth(Date targetMonth) {
        this.targetMonth = targetMonth;
    }

    /**
     * 処理日を取得する。
     *
     * @return 処理日
     */
    public Date getProcessDate() {
        return processDate;
    }

    /**
     * 処理日をセットする。
     *
     * @param processDate 処理日
     */
    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }
    
    /**
     * レジ担当者を取得する。
     *
     * @return レジ担当者
     */
    public MstStaff getStaff() {
        return staff;
    }

    /**
     * レジ担当者をセットする。
     *
     * @param staff レジ担当者
     */
    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }

    /**
     * 支払方法を取得する。
     *
     * @return 支払方法
     */
    public Integer getPaymentMethodID() {
        return paymentMethodID;
    }

    /**
     * 支払方法をセットする。
     *
     * @param paymentMethodID 支払方法
     */
    public void setPaymentMethodID(Integer paymentMethodID) {
        this.paymentMethodID = paymentMethodID;
    }

    /**
     * 未処理を取得する。
     *
     * @return 未処理
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * 入会店舗をセットする。
     *
     * @param processed 入会店舗
     */
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    /**
     * 処理済みを取得する。
     *
     * @return 処理済み
     */
    public boolean isUntreated() {
        return untreated;
    }

    /**
     * 入会店舗をセットする。
     *
     * @param untreated 入会店舗
     */
    public void setUntreated(boolean untreated) {
        this.untreated = untreated;
    }

    @Override
    public String toString() {
        return ((this.getShop() != null && this.getShop().getShopName() != null)) ? this.getShop().getShopName() : "";
    }

    /**
     * コンストラクタ
     */
    public DataMonthlyBatchLog() {
    }

    /**
     * 一括処理ログからデータをセットする。
     *
     * @param data 一括処理ログ
     */
    public void setData(DataMonthlyBatchLog data) {
        this.setBatchID(data.getBatchID());
        this.setShop(data.getShop());
        this.setTargetMonth(data.getTargetMonth());
        this.setProcessDate(data.getProcessDate());
        this.setPaymentMethodID(data.getPaymentMethodID());
        this.setProcessed(data.isProcessed());
    }

    /**
     * 一括処理ログをArrayListに読み込む。
     * @param con
     * @return 
     * @throws java.sql.SQLException
     */
    public ArrayList<DataMonthlyBatchLog> load(ConnectionWrapper con) throws SQLException {
        ArrayList<DataMonthlyBatchLog> listDataBatch = new ArrayList<DataMonthlyBatchLog>();
        ResultSetWrapper rs = con.executeQuery(
                this.getSelectSQL());

        while (rs.next()) {
            DataMonthlyBatchLog batch = new DataMonthlyBatchLog();
            DataMonthlyBatchDetailLog detail = new DataMonthlyBatchDetailLog();
            batch.setData(rs);
            detail.setData(rs);
            batch.add(detail);
            listDataBatch.add(batch);
        }

        rs.close();
        return listDataBatch;
    }

    /**
     * ResultSetWrapperからデータをセットする
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setBatchID(rs.getInt("batch_id"));
        MstShop mShop = new MstShop();
        mShop.setShopID(rs.getInt("shop_id"));
        mShop.setShopName(rs.getString("shop_name"));
        this.setShop(mShop);
        this.setTargetMonth(rs.getDate("target_month"));
        this.setProcessDate(rs.getDate("process_date"));
        this.setPaymentMethodID(rs.getInt("payment_method_id"));
        this.setProcessed(rs.getBoolean("is_processed"));
    }

    /**
     * @param con ConnectionWrapper
     * @return true - 成功
     * @throws java.sql.SQLException SQLException
     */
    public boolean registAll(ConnectionWrapper con) throws SQLException {
        if (!this.regist(con)) {
            return false;
        }

        for (DataMonthlyBatchDetailLog detail : this) {
            detail.setBatchID(this.getBatchID());
            if (!detail.regist(con)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 一括処理ログを登録する。
     *
     * @return true - 成功
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {

        this.setNewBatchID(con);
        if (con.executeUpdate(this.getInsertSQL()) != 1) {
            return false;
        }
        return true;
    }

    /**
     * 一括処理ログが存在するかチェックする。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getBatchID() != null) {
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
    
    /**
	 * 処理IDをセットする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setNewBatchID(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getNewBatchIDSQL());
		
		if(rs.next())
		{
			this.setBatchID(rs.getInt("new_batch_id"));
		}
		
		rs.close();
	}

    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append("select mm.*\n");
        sql.append(",shop.shop_id\n");
        sql.append(",shop.shop_name\n");
        sql.append(",customer.customer_id\n");
        sql.append(",customer.customer_no\n");
        sql.append(",customer.customer_name1\n");
        sql.append(",customer.customer_name2\n");
        sql.append(",plan.plan_id\n");
        sql.append(",plan.plan_name\n");
        sql.append(",course.course_id\n");
        sql.append(",course.course_name\n");
        sql.append(",mup.price\n");
        sql.append(",course.num\n");
        sql.append(",course.praise_time_limit\n");
        sql.append(",bat.batch_id\n");
        sql.append(",bat.target_month\n");
        sql.append(",bat.process_date\n");
        sql.append(",bat.payment_method_id\n");
        sql.append(",bat.valid_date\n");
        sql.append(",bat.slip_no\n");
        sql.append(",(case when bat.batch_id is null then 0\n");
        sql.append("	else 1\n");
        sql.append("	end \n");
        sql.append("  )as is_processed\n");
        sql.append("from data_month_member mm\n");
        sql.append("inner join mst_shop shop\n");
        sql.append("on mm.shop_id = shop.shop_id\n");
        sql.append("inner join mst_customer customer\n");
        sql.append("on mm.customer_id = customer.customer_id\n");
        sql.append("inner join mst_plan plan\n");
        sql.append("on mm.plan_id = plan.plan_id\n");
        sql.append("inner join mst_course course\n");
        sql.append("on plan.course_id = course.course_id\n");
        sql.append("inner join mst_use_product mup\n");
        sql.append("on course.course_id = mup.product_id\n");
        sql.append("and course.delete_date is null\n");
        sql.append("left join \n");
        sql.append("   (\n");
        sql.append("	select batch.*, batch_detail.month_contract_id\n");
        sql.append("   , batch_detail.valid_date\n");
        sql.append("   , batch_detail.slip_no\n");
        sql.append("	from data_monthly_batch_log batch\n");
        sql.append("	inner join data_monthly_batch_detail_log batch_detail\n");
        sql.append("	on batch.batch_id = batch_detail.batch_id\n");
        sql.append("	where batch.delete_date is null\n");
        sql.append("    ) bat on bat.month_contract_id = mm.month_contract_id\n");
        sql.append("    and to_char(").append(SQLUtil.convertForSQL(this.getTargetMonth())).append("::date, 'YYYY-MM') = to_char(bat.target_month, 'YYYY-MM') \n");
        sql.append("where mm.delete_date is null\n");
        sql.append("     and mup.delete_date is null\n");
        sql.append("     and mup.shop_id = mm.shop_id\n");
        sql.append("     and mup.product_division = 3\n");
        if(this.isProcessed() && (!this.isUntreated())) {
            sql.append("	and bat.batch_id is not null\n");
        }
        if((!this.isProcessed()) && this.isUntreated()) {
            sql.append("	and bat.batch_id is null\n");
        }
        if (this.getShop() != null && this.getShop().getShopID() != null && this.getShop().getShopID() != 0) {
            sql.append("	and mm.shop_id =").append(SQLUtil.convertForSQL(this.getShop().getShopID())).append("\n");
        }
        sql.append("	and (\n");
        sql.append("                (mm.status = 1)\n");
        if (this.getTargetMonth() != null) {
            sql.append("                or(").append(SQLUtil.convertForSQL(this.getTargetMonth())).append("::date <= mm.stop_date::date \n");
            sql.append("                   or ").append(SQLUtil.convertForSQL(this.getTargetMonth())).append("::date <= mm.withdrawal_date::date \n");
            sql.append("                   )\n");
        }
        sql.append("            )\n");
        if (this.getTargetMonth() != null) {
            sql.append("    and to_char(").append(SQLUtil.convertForSQL(this.getTargetMonth())).append("::date, 'YYYY-MM') >= to_char(mm.join_date, 'YYYY-MM') \n");
        }
        if(this.isProcessed() && this.isUntreated()) {
            sql.append("    and (bat.batch_id is not null \n");
            sql.append("	or (bat.batch_id is null \n");
            sql.append("	and mm.month_contract_id not in \n");
            sql.append("	(select batch_detail.month_contract_id \n");
            sql.append("	from data_monthly_batch_detail_log batch_detail)))\n");
        }
        sql.append("order by is_processed, mm.shop_id, mm.month_contract_id\n");

        return sql.toString();
    }

    /**
     * Insert文を取得する。
     *
     * @return Insert文
     */
    private String getInsertSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append("insert into data_monthly_batch_log\n");
        sql.append("(batch_id, \n");
        sql.append("shop_id, \n");
        sql.append("target_month, \n");
        sql.append("process_date, \n");
        sql.append("staff_id, \n");
        sql.append("payment_method_id,\n");
        sql.append("insert_date, update_date, delete_date)\n");
        sql.append("select\n");
        sql.append(SQLUtil.convertForSQL(this.getBatchID())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getShop().getShopID())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getTargetMonth())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getProcessDate())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getStaff().getStaffID())).append(",\n");
        sql.append(SQLUtil.convertForSQL(this.getPaymentMethodID())).append(",\n");
        sql.append("current_timestamp, current_timestamp, null\n");

        return sql.toString();
    }

    /**
     * 処理IDを取得するＳＱＬ文
     * @return 
     */
    public String getNewBatchIDSQL()
	{
		return	"select coalesce(max(batch_id), 0) + 1 as new_batch_id\n" +
				"from data_monthly_batch_log\n";
	}

}

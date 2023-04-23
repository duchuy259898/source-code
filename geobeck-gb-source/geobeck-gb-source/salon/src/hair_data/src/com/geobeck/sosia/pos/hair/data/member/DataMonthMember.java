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
 * ������f�[�^
 *
 * @author lvtu
 */
public class DataMonthMember {

    /**
     * ����X��
     */
    protected MstShop mShop = null;
    /**
     * ���_��ID
     */
    protected Integer monthContractID = null;
    /**
     * �ڋq
     */
    protected MstCustomer mCustomer = null;
    /**
     * �v����
     */
    protected MstPlan mPlan = null;
    /**
     * �����
     */
    protected Date joinDate = null;
    /**
     * ��~��
     */
    protected Date stopDate = null;
    /**
     * �މ��
     */
    protected Date withdrawalDate = null;
    /**
     * �X�e�[�^�X
     */
    protected Integer status = null;
    /**
     * ����
     */
    protected String memo = "";
    
    /**
     * �X�e�[�^�X
     */
    protected String statusList = "";

    /**
     * �����From
     */
    protected Date joinDateFrom = null;
    /**
     * ��~��From
     */
    protected Date stopDateFrom = null;
    /**
     * �މ��From
     */
    protected Date withdrawalDateFrom = null;
    
    /**
     * �����To
     */
    protected Date joinDateTo = null;
    /**
     * ��~��To
     */
    protected Date stopDateTo = null;
    /**
     * �މ��To
     */
    protected Date withdrawalDateTo = null;

    /**
     * ����X�܂��擾����B
     *
     * @return ����X��
     */
    public MstShop getmShop() {
        return mShop;
    }

    /**
     * ����X�܂��Z�b�g����B
     *
     * @param mShop ����X��
     */
    public void setmShop(MstShop mShop) {
        this.mShop = mShop;
    }

    /**
     * ���_��ID���擾����B
     *
     * @return ���_��ID
     */
    public Integer getMonthContractID() {
        return monthContractID;
    }

    /**
     * ���_��ID���Z�b�g����B
     *
     * @param monthContractID ���_��ID
     */
    public void setMonthContractID(Integer monthContractID) {
        this.monthContractID = monthContractID;
    }

    /**
     * �ڋq���擾����B
     *
     * @return �ڋq
     */
    public MstCustomer getmCustomer() {
        return mCustomer;
    }

    /**
     * �ڋq���Z�b�g����B
     *
     * @param mCustomer �ڋq
     */
    public void setmCustomer(MstCustomer mCustomer) {
        this.mCustomer = mCustomer;
    }

    /**
     * �v�������擾����B
     *
     * @return �v����
     */
    public MstPlan getmPlan() {
        return mPlan;
    }

    /**
     * �v�������Z�b�g����B
     *
     * @param mPlan �v����
     */
    public void setmPlan(MstPlan mPlan) {
        this.mPlan = mPlan;
    }

    /**
     * ��������擾����B
     *
     * @return �����
     */
    public Date getJoinDate() {
        return joinDate;
    }

    /**
     * ��������Z�b�g����B
     *
     * @param joinDate �����
     */
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    /**
     * ����X�܂��擾����B
     *
     * @return ����X��
     */
    public Date getStopDate() {
        return stopDate;
    }

    /**
     * ��~�����Z�b�g����B
     *
     * @param stopDate ��~��
     */
    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    /**
     * �މ�����擾����B
     *
     * @return �މ��
     */
    public Date getWithdrawalDate() {
        return withdrawalDate;
    }

    /**
     * �މ�����Z�b�g����B
     *
     * @param withdrawalDate �މ��
     */
    public void setWithdrawalDate(Date withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    /**
     * �X�e�[�^�X���擾����B
     *
     * @return �X�e�[�^�X
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * �X�e�[�^�X���Z�b�g����B
     *
     * @param status �X�e�[�^�X
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * �������擾����B
     *
     * @return ����
     */
    public String getMemo() {
        return memo;
    }

    /**
     * �������Z�b�g����B
     *
     * @param memo ���i�h�c
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
     * �R���X�g���N�^
     */
    public DataMonthMember() {
    }

    /**
     * ������f�[�^����f�[�^���Z�b�g����B
     *
     * @param data ������f�[�^
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
     * ResultSetWrapper����f�[�^���Z�b�g����
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
     * ������f�[�^��o�^����B
     *
     * @return true - ����
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
     * ������f�[�^���폜����B�i�_���폜�j
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
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
     * ������f�[�^�����݂��邩�`�F�b�N����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
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
     * ������f�[�^
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - �擾��������
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
     * Select�����擾����B
     *
     * @return Select��
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
     * Select�����擾����B
     *
     * @return Select��
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
     * Insert�����擾����B
     *
     * @return Insert��
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
     * Update�����擾����B
     *
     * @return Update��
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
     * �폜�pUpdate�����擾����B
     *
     * @return �폜�pUpdate��
     */
    private String getDeleteSQL() {
        return "update data_month_member\n"
                + "set\n"
                + "delete_date = current_timestamp\n"
                + "where month_contract_id = " + SQLUtil.convertForSQL(this.getMonthContractID()) + "\n";
    }
}

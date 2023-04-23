/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.member;

import com.geobeck.sosia.pos.hair.data.account.Course;
import com.geobeck.sosia.pos.hair.master.product.MstPlan;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.Date;

/**
 *�ꊇ�������O
 * @author lvtu
 */
public class DataMonthlyBatchDetailLog {
    /**
     * ����ID
     */
    protected Integer batchID = null;
    /**
     * ���_��
     */
    protected DataMonthMember dataMonthMember = null;
    /**
     * �ڋq
     */
    protected MstCustomer customer = null;
    /**
     * �R�[�X
     */
    protected Course course = null;
    /**
     * �L������
     */
    protected Date validDate = null;
    /**
     * �`�[NO
     */
    protected Integer slipNO = null;

    /**
     * ����ID���擾����B
     *
     * @return ����ID
     */
    public Integer getBatchID() {
        return batchID;
    }

    /**
     * ����ID���Z�b�g����B
     *
     * @param batchID ����ID
     */
    public void setBatchID(Integer batchID) {
        this.batchID = batchID;
    }

    /**
     * ���_����擾����B
     *
     * @return ���_��
     */
    public DataMonthMember getDataMonthMember() {
        return dataMonthMember;
    }

    /**
     * ���_����Z�b�g����B
     *
     * @param dataMonthMember ���_��
     */
    public void setDataMonthMember(DataMonthMember dataMonthMember) {
        this.dataMonthMember = dataMonthMember;
    }

    /**
     * �ڋq���擾����B
     *
     * @return �ڋq
     */
    public MstCustomer getCustomer() {
        return customer;
    }

    /**
     * �ڋq���Z�b�g����B
     *
     * @param customer �ڋq
     */
    public void setCustomer(MstCustomer customer) {
        this.customer = customer;
    }

    /**
     * �R�[�X���擾����B
     *
     * @return �R�[�X
     */
    public Course getCourse() {
        return course;
    }

    /**
     * �R�[�X���Z�b�g����B
     *
     * @param course �R�[�X
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * �L���������擾����B
     *
     * @return �L������
     */
    public Date getValidDate() {
        return validDate;
    }

    /**
     * �L���������Z�b�g����B
     *
     * @param validDate �L������
     */
    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    /**
     * �`�[NO���擾����B
     *
     * @return �`�[NO
     */
    public Integer getSlipNO() {
        return slipNO;
    }

    /**
     * �`�[NO���Z�b�g����B
     *
     * @param slipNO �`�[NO
     */
    public void setSlipNO(Integer slipNO) {
        this.slipNO = slipNO;
    }
    
    
    /**
     * �R���X�g���N�^
     */
    public DataMonthlyBatchDetailLog() {
    }

    /**
     * ������f�[�^����f�[�^���Z�b�g����B
     *
     * @param data ������f�[�^
     */
    public void setData(DataMonthlyBatchDetailLog data) {
        this.setBatchID(data.getBatchID());
        this.setDataMonthMember(data.getDataMonthMember());
        this.setCustomer(data.getCustomer());
        this.setCourse(data.getCourse());
        this.setValidDate(data.getValidDate());
        this.setSlipNO(data.getSlipNO());
    }

    /**
     * ResultSetWrapper����f�[�^���Z�b�g����
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setBatchID(rs.getInt("batch_id"));
        DataMonthMember datamm = new DataMonthMember();
        datamm.setMonthContractID(rs.getInt("month_contract_id"));
        datamm.setJoinDate(rs.getDate("join_date"));
        MstShop shop = new MstShop();
        shop.setShopID(rs.getInt("shop_id"));
        shop.setShopName(rs.getString("shop_name"));
        datamm.setmShop(shop);
        MstPlan plan = new MstPlan();
        plan.setPlanId(rs.getInt("plan_id"));
        plan.setPlanName(rs.getString("plan_name"));
        datamm.setmPlan(plan);
        this.setDataMonthMember(datamm);
        MstCustomer mCustomer = new MstCustomer();
        mCustomer.setCustomerID(rs.getInt("customer_id"));
        mCustomer.setCustomerNo(rs.getString("customer_no"));
        mCustomer.setCustomerName(new String[]{rs.getString("customer_name1"), rs.getString("customer_name2")});
        this.setCustomer(mCustomer);
        Course mCourse = new Course();
        mCourse.setCourseId(rs.getInt("course_id"));
        mCourse.setPrice(rs.getLong("price"));
        mCourse.setNum(rs.getInt("num"));
        mCourse.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
        this.setCourse(mCourse);
        this.setValidDate(rs.getDate("valid_date"));
        this.setSlipNO(rs.getInt("slip_no"));
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
     * ������f�[�^�����݂��邩�`�F�b�N����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getDataMonthMember() == null || this.getBatchID() == null) {
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
     * Select�����擾����B
     *
     * @return Select��
     */
    private String getSelectSQL() {
        String sql = "select detail.*\n"
                + "from data_monthly_batch_detail_log detail\n"
                + "where detail.delete_date is null\n"
                + "and batch_id =" + SQLUtil.convertForSQL(this.getBatchID()) + "\n"
                + "and month_contract_id =" + SQLUtil.convertForSQL(this.getDataMonthMember().getMonthContractID()) + "\n";

        return sql;
    }

    /**
     * Insert�����擾����B
     *
     * @return Insert��
     */
    private String getInsertSQL() {
        return "insert into data_monthly_batch_detail_log\n"
                + "(batch_id, \n"
                + "month_contract_id, \n"
                + "customer_id, \n"
                + "course_id, \n"
                + "course_value,\n"
                + "num,\n"
                + "valid_date,\n"
                + "slip_no,\n"
                + "insert_date, update_date, delete_date)\n"
                + "select\n"
                + SQLUtil.convertForSQL(this.getBatchID()) + ",\n"
                + SQLUtil.convertForSQL(this.getDataMonthMember().getMonthContractID()) + ",\n"
                + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ",\n"
                + SQLUtil.convertForSQL(this.getCourse().getCourseId()) + ",\n"
                + SQLUtil.convertForSQL(this.getCourse().getPrice()) + ",\n"
                + SQLUtil.convertForSQL(this.getCourse().getNum()) + ",\n"
                + SQLUtil.convertForSQL(this.getValidDate()) + ",\n"
                + SQLUtil.convertForSQL(this.getSlipNO()) + ",\n"
                + "current_timestamp, current_timestamp, null\n";
    }

    /**
     * Update�����擾����B
     *
     * @return Update��
     */
    private String getUpdateSQL() {
        return "update data_monthly_batch_detail_log\n"
                + "set\n"
                + "customer_id =" + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ", \n"
                + "course_id =" + SQLUtil.convertForSQL(this.getCourse().getCourseId()) + ", \n"
                + "course_value =" + SQLUtil.convertForSQL(this.getCourse().getPrice()) + ",\n"
                + "num =" + SQLUtil.convertForSQL(this.getCourse().getNum()) + ",\n"
                + "valid_date = " + SQLUtil.convertForSQL(this.getValidDate()) + ",\n"
                + "slip_no = " + SQLUtil.convertForSQL(this.getSlipNO()) + ",\n"
                + "where batch_id =" + SQLUtil.convertForSQL(this.getBatchID()) +"\n"
                + "and month_contract_id = " + SQLUtil.convertForSQL(this.getDataMonthMember().getMonthContractID()) + "\n";
    }


}

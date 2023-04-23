/*
 * DataSalesProportionally.java
 *
 * Created on 2007/10/16, 9:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.account;

import java.sql.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.hair.data.product.*;

/**
 *
 * @author kanemoto
 */
public class DataSalesProportionally {

    protected DataSalesDetail dataSalesDetail = null;
    /** ���Z�ڍ� */
    protected DataProportionally proportionally = new DataProportionally();
    /** �� */
    protected MstStaff staff = new MstStaff();
    /** �X�^�b�t */
    protected boolean designated = false;
    /** �w�� */
    protected Integer point = null;
    /** �|�C���g */
    protected Integer rate = null;

    /** ��ӘA�ԁi��L�[�̈ꕔ�j */
    private Integer seqNum = null;

    /** ���� */
    /** Creates a new instance of DataSalesProportionally */
    public DataSalesProportionally() {
    }

    /**
     * �X�܂��擾����B
     * @return �X��
     */
    public MstShop getShop() {
        if (dataSalesDetail != null) {
            return dataSalesDetail.getShop();
        } else {
            return null;
        }
    }

    /**
     * �`�[No���擾����
     */
    public Integer getSlipNo() {
        if (dataSalesDetail != null) {
            return dataSalesDetail.getSlipNo();
        } else {
            return null;
        }
    }

    /**
     * �`�[�ڍ�No.���擾����B
     * @return �`�[�ڍ�No.
     */
    public Integer getSlipDetailNo() {
        if (dataSalesDetail != null) {
            return dataSalesDetail.getSlipDetailNo();
        } else {
            return null;
        }
    }

    /**
     * �`�[�ڍׂ��擾����B
     * @return �`�[�ڍ�
     */
    public DataSalesDetail getSalesDetail() {
        return dataSalesDetail;
    }

    /**
     * �`�[�ڍׂ��Z�b�g����B
     * @param dataSalesDetail �`�[�ڍ�
     */
    public void setDataSalesDetail(DataSalesDetail dataSalesDetail) {
        this.dataSalesDetail = dataSalesDetail;
    }

    /**
     * �����擾����B
     * @return ��
     */
    public DataProportionally getProportionally() {
        return proportionally;
    }

    /**
     * ��ID���擾����B
     * @return ��ID
     */
    public Integer getDataProportionallyID() {
        if (proportionally != null) {
            return proportionally.getDataProportionallyID();
        }
        return null;
    }

    /**
     * �����Z�b�g����B
     * @param technic ��
     */
    public void setProportionally(DataProportionally proportionally) {
        this.proportionally = proportionally;
    }

    /**
     * �X�^�b�t���擾����B
     * @return �X�^�b�t
     */
    public MstStaff getStaff() {
        return staff;
    }

    /**
     * �X�^�b�t���Z�b�g����B
     * @param staff �X�^�b�t
     */
    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }

    /**
     * �w���t���O���擾����
     * @return �w�� true:�w�� false:�t���[
     */
    public boolean getDesignated() {
        return designated;
    }

    /**
     * �w���t���O���Z�b�g����
     * @param designated �w���t���O
     */
    public void setDesignated(boolean designated) {
        this.designated = designated;
    }

    /**
     * �|�C���g���擾����
     * return ���|�C���g
     */
    public Integer getPoint() {
        return point;
    }

    /**
     * �|�C���g���Z�b�g����
     * @param point ���|�C���g
     */
    public void setPoint(Integer point) {
        this.point = point;
    }

    /**
     * �������擾����
     * return ����
     */
    public Integer getRate() {
        return rate;
    }

    /**
     * �������Z�b�g����
     * @param rate ����
     */
    public void setRate(Integer rate) {
        this.rate = rate;
    }

    /**
     * @return the seqNum
     */
    public Integer getSeqNum() {
        return seqNum;
    }

    /**
     * @param seqNum the seqNum to set
     */
    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    /**
     * �\����f�[�^���Z�b�g����B
     * @param dr �\����f�[�^
     */
    public void set(DataSalesProportionally dsp) {
        this.setDataSalesDetail(dsp.getSalesDetail());
        this.setProportionally(dsp.getProportionally());
        this.setDesignated(dsp.getDesignated());
        this.setStaff(dsp.getStaff());
        this.setPoint(dsp.getPoint());
        this.setRate(dsp.getRate());
        this.setSeqNum(dsp.getSeqNum());
    }

    /**
     * �f�[�^��o�^����B
     * @param con
     * @throws java.sql.SQLException
     * @return
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        String sql = "";

        if (isExists(con)) {
            sql = this.getUpdateSQL();
        } else {
            sql = this.getInsertSQL();
        }

        if (con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * �f�[�^���폜����B�i�_���폜�j
     * @param con
     * @throws java.sql.SQLException
     * @return
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
     * �f�[�^�����݂��邩�`�F�b�N����B
     * @param con
     * @throws java.sql.SQLException
     * @return
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getSlipNo() == null
                || this.getSlipDetailNo() == null
                || this.getDataProportionallyID() == null) {
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
     * @return
     */
    private String getSelectSQL() {
        return "select\n"
                + "*\n"
                + "from\n"
                + "data_sales_proportionally\n"
                + "where\n"
                + "shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo()) + "\n"
                + "and data_proportionally_id = " + SQLUtil.convertForSQL(this.getDataProportionallyID()) + "\n"
                + "and seq_num = " + SQLUtil.convertForSQL(this.getSeqNum()) + "\n"
                + ";\n";
    }

    /**
     * Insert�����擾����B
     * @return
     */
    private String getInsertSQL() {
        return "insert into data_sales_proportionally\n"
                + "(shop_id, slip_no, slip_detail_no, data_proportionally_id, seq_num,\n"
                + "designated_flag, staff_id, point, ratio,\n"
                + "insert_date, update_date, delete_date)\n"
                + "values(\n"
                + SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n"
                + SQLUtil.convertForSQL(this.getSlipNo()) + ",\n"
                + SQLUtil.convertForSQL(this.getSlipDetailNo()) + ",\n"
                + SQLUtil.convertForSQL(this.getDataProportionallyID()) + ",\n"
                + "(\n"
                + "select coalesce(max(seq_num), 0) + 1 from data_sales_proportionally\n"
                + "where\n"
                + "shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo()) + "\n"
                + "and data_proportionally_id = " + SQLUtil.convertForSQL(this.getDataProportionallyID()) + "\n"
                + "),\n"
                + SQLUtil.convertForSQL(this.getDesignated()) + ",\n"
                + (staff != null ? SQLUtil.convertForSQL(this.getStaff().getStaffID()) : null) + ",\n"
                + SQLUtil.convertForSQL(this.getPoint()) + ",\n"
                + SQLUtil.convertForSQL(this.getRate()) + ",\n"
                + "current_timestamp, current_timestamp, null );";
    }

    /**
     * Update�����擾����B
     * @return
     */
    private String getUpdateSQL() {
        return "update\n"
                + "data_sales_proportionally\n"
                + "set\n"
                + "designated_flag = " + SQLUtil.convertForSQL(this.getDesignated()) + ",\n"
                + "staff_id = " + (staff != null ? SQLUtil.convertForSQL(this.getStaff().getStaffID()) : null) + ", \n"
                + "point = " + SQLUtil.convertForSQL(this.getPoint()) + ", \n"
                + "ratio = " + SQLUtil.convertForSQL(this.getRate()) + ", \n"
                + "update_date = current_timestamp, \n"
                + "delete_date = null\n"
                + "where\n"
                + "shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo()) + "\n"
                + "and data_proportionally_id = " + SQLUtil.convertForSQL(this.getDataProportionallyID()) + "\n"
                + "and seq_num = " + SQLUtil.convertForSQL(this.getSeqNum()) + "\n"
                + ";\n";
    }

    /**
     * �폜�pUpdate�����擾����B
     * @return
     */
    private String getDeleteSQL() {
        return "update data_sales_proportionally\n"
                + "set\n"
                + "update_date = current_timestamp,\n"
                + "delete_date = current_timestamp\n"
                + "where \n"
                + "shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo()) + "\n"
                + "and data_proportionally_id = " + SQLUtil.convertForSQL(this.getDataProportionallyID()) + "\n"
                + "and seq_num = " + SQLUtil.convertForSQL(this.getSeqNum()) + "\n"
                + ";\n";
    }

}

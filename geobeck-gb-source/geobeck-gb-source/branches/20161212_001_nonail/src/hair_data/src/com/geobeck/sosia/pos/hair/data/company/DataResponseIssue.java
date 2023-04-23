/*
 * DataResponseIssue.java
 *
 * Created on 2007/08/31, 10:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.company;

import java.util.*;
import java.sql.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.hair.master.company.*;

/**
 * ���X�|���X���s�f�[�^
 * @author kanemoto
 */
public class DataResponseIssue {

    protected Integer responseIssueId;
    /** ���X�|���X���sID */
    protected MstResponse response;
    /** ���X�|���X */
    protected Integer shop_id;
    /** �X��ID */
    protected MstStaff staff;
    /** ���s�X�^�b�t */
    protected GregorianCalendar circulationMonthlyDate;
    /** ���s�N�� */
    protected Integer circulationNumber;
    /** ���s���� */
    protected GregorianCalendar registDate;

    /** �o�^�� */
    /** Creates a new instance of DataResponseIssue */
    public DataResponseIssue() {
    }

    /**
     * ���X�|���X���sID���擾����
     * @return ���X�|���X���sID
     */
    public Integer getResponseIssueId() {
        return this.responseIssueId;
    }

    /**
     * ���X�|���X���sID��o�^����
     * @param responseIssueId ���X�|���X���sID
     */
    public void setResponseIssueId(Integer responseIssueId) {
        this.responseIssueId = responseIssueId;
    }

    /**
     * ���X�|���X���擾����
     * @return ���X�|���X
     */
    public MstResponse getResponse() {
        return this.response;
    }

    /**
     * ���X�|���X��o�^����
     * @param response ���X�|���X
     */
    public void setResponse(MstResponse response) {
        this.response = response;
    }

    /**
     * ���X�|���X���擾����
     * @return ���X�|���X
     */
    public Integer getShopID() {
        return this.shop_id;
    }

    /**
     * �X��ID��o�^����
     * @param shop_id �X��ID
     */
    public void setShopID(Integer shop_id) {
        this.shop_id = shop_id;
    }

    /**
     * ���s�X�^�b�t���擾����
     * @return ���s�X�^�b�t
     */
    public MstStaff getStaff() {
        return this.staff;
    }

    /**
     * ���s�X�^�b�t��o�^����
     * @param response ���s�X�^�b�t
     */
    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }

    /**
     * ���s�N�����擾����
     * @return ���s�N��
     */
    public GregorianCalendar getCirculationMonthlyDate() {
        return this.circulationMonthlyDate;
    }

    /**
     * ���s�N����o�^����
     * @param circulationMonthlyDate ���s�N��
     */
    public void setCirculationMonthlyDate(GregorianCalendar circulationMonthlyDate) {
        this.circulationMonthlyDate = circulationMonthlyDate;
    }

    /**
     * ���s�������擾����
     * @return ���s����
     */
    public Integer getCirculationNumber() {
        return this.circulationNumber;
    }

    /**
     * ���s������o�^����
     * @param circulationNumber ���s����
     */
    public void setCirculationNumber(Integer circulationNumber) {
        this.circulationNumber = circulationNumber;
    }

    /**
     * �o�^�����擾����
     * @return �o�^��
     */
    public GregorianCalendar getRegistDate() {
        return this.registDate;
    }

    /**
     * �o�^����o�^����
     * @param registDate �o�^��
     */
    public void setRegistDate(GregorianCalendar registDate) {
        this.registDate = registDate;
    }

    /**
     * DataResponseIssue�B
     * @param dri DataResponseIssue
     */
    public void setData(DataResponseIssue dri) {
        this.setResponseIssueId(dri.getResponseIssueId());
        this.setResponse(dri.getResponse());
        this.setShopID(dri.getShopID());
        this.setStaff(dri.getStaff());
        this.setCirculationMonthlyDate(dri.getCirculationMonthlyDate());
        this.setCirculationNumber(dri.getCirculationNumber());
        this.setRegistDate(dri.getRegistDate());
    }

    /**
     * ResultSetWrapper����f�[�^���Z�b�g����B
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setResponseIssueId(rs.getInt("response_issue_id"));
        MstResponse mr = new MstResponse();
        mr.setData(rs);
        this.setResponse(mr);
        this.setShopID(rs.getInt("shop_id"));
        MstStaff ms = new MstStaff();
        ms.setStaffNo(rs.getString("staff_no"));
        ms.setStaffName(0, rs.getString("staff_name1"));
        ms.setStaffName(1, rs.getString("staff_name2"));
        this.setStaff(ms);
        this.setCirculationMonthlyDate(rs.getGregorianCalendar("circulation_monthly_date"));
        this.setCirculationNumber(rs.getInt("circulation_number"));
        this.setRegistDate(rs.getGregorianCalendar("regist_date"));
    }

    /**
     * ���X�|���X�f�[�^��o�^����B
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        String sql = "";

        // ���R�[�h�����݂��Ȃ��ꍇ�ɂ͐V�K�o�^���s��
        if (!isExists(con)) {
            sql = this.getInsertSQL();
        } else {
            sql = this.getUpdateSQL();
        }

        if (con.executeUpdate(sql) == 1) {
            return true;
        }
        return false;
    }

    /**
     * ���X�|���X�f�[�^���폜����B�i�_���폜�j
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean delete(ConnectionWrapper con) throws SQLException {
        String sql = "";

        // ���R�[�h�����݂��Ȃ��ꍇ�ɂ͏������s��Ȃ�
        if (!isExists(con)) {
            return false;
        }

        // �폜�pSQL���擾����
        sql = this.getDeleteSQL();

        if (con.executeUpdate(sql) == 1) {
            return true;
        }
        return false;
    }

    /**
     * ���X�|���X�f�[�^�����݂��邩�`�F�b�N����B
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getResponseIssueId() == null || this.getResponseIssueId() == 0) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getExistsSelectSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ���X�|���X�f�[�^���ݎ擾SQL
     */
    public String getExistsSelectSQL() {
        return "select\n"
                + "*\n"
                + "from\n"
                + "data_response_issue\n"
                + "where\n"
                + "response_issue_id = " + SQLUtil.convertForSQL(this.getResponseIssueId()) + "\n"
                + ";\n";
    }

    /**
     * �o�ދ΃f�[�^�}��SQL���擾����
     */
    public String getInsertSQL() {
        return "insert into data_response_issue\n"
                + "(response_issue_id, response_id, shop_id, staff_id, \n"
                + "circulation_monthly_date, circulation_number, regist_date, insert_date, \n"
                + "update_date, delete_date)\n"
                + "values(\n"
                + "( select max( response_issue_id ) from data_response_issue ) + 1,\n"
                + SQLUtil.convertForSQL(this.getResponse().getResponseID()) + ",\n"
                + SQLUtil.convertForSQL(this.getShopID()) + ",\n"
                + ((this.getStaff() == null) ? "null" : SQLUtil.convertForSQL(this.getStaff().getStaffID())) + ",\n"
                + ((this.getCirculationMonthlyDate() == null) ? "null" : SQLUtil.convertForSQL(this.getCirculationMonthlyDate())) + ",\n"
                + SQLUtil.convertForSQL(this.getCirculationNumber()) + ",\n"
                + SQLUtil.convertForSQL(this.getRegistDate()) + ",\n"
                + "current_timestamp, current_timestamp, null );\n";
    }

    /**
     * �o�ދ΃f�[�^�X�VSQL���擾����
     */
    public String getUpdateSQL() {
        return "update data_response_issue\n"
                + "set\n"
                + "response_id = " + SQLUtil.convertForSQL(this.getResponse().getResponseID()) + ", \n"
                + "shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + ", \n"
                + ((this.getStaff() == null) ? ("staff_id = null,\n") : ("staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ", \n"))
                + "circulation_monthly_date = " + SQLUtil.convertForSQL(this.getCirculationMonthlyDate()) + ", \n"
                + "circulation_number = " + SQLUtil.convertForSQL(this.getCirculationNumber()) + ", \n"
                + "regist_date = " + SQLUtil.convertForSQL(this.getRegistDate()) + ", \n"
                + "update_date = current_timestamp\n"
                + "where\n"
                + "response_issue_id = " + SQLUtil.convertForSQL(this.getResponseIssueId()) + "\n"
                + ";\n";
    }

    /**
     * �o�ދ΃f�[�^�폜SQL���擾����
     */
    public String getDeleteSQL() {
        return "update data_response_issue\n"
                + "set update_date = current_timestamp, \n"
                + "delete_date = current_timestamp\n"
                + "where\n"
                + "response_issue_id = " + SQLUtil.convertForSQL(this.getResponseIssueId()) + "\n"
                + ";\n";
    }
}

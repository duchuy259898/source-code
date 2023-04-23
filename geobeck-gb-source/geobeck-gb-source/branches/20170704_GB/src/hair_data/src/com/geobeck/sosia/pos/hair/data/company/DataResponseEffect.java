/*
 * DataResponseEffect.java
 *
 * Created on 2007/08/29, 13:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.company;

import java.sql.*;
import java.util.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.hair.master.company.*;

/**
 *
 * @author kanemoto
 */
public class DataResponseEffect {

    protected Integer slipNo = null;	// �`�[No
    protected MstResponse response = null;	// ���X�|���X�}�X�^
    protected Integer shopID = null;	// �X��ID
    protected DataResponseIssue responseIssue = null;	// ���X�|���X���s�f�[�^
    protected GregorianCalendar dataResponseDate = null;	// ���X�|���X�����

    /** Creates a new instance of DataResponseEffect */
    public DataResponseEffect() {
    }

    /**
     * �`�[No���擾����
     * @return �`�[No
     */
    public Integer getSlipNo() {
        return this.slipNo;
    }

    /**
     * �`�[No��o�^����
     * @param slipNo �`�[No
     */
    public void setSlipNo(Integer slipNo) {
        this.slipNo = slipNo;
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
     * �X��ID���擾����
     * @return ���X�|���X
     */
    public Integer getShopID() {
        return this.shopID;
    }

    /**
     * �X��ID��o�^����
     * @param shopID ���X�|���X
     */
    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    /**
     * ���X�|���X���s�f�[�^���擾����
     * @return ���X�|���X
     */
    public DataResponseIssue getResponseIssue() {
        return this.responseIssue;
    }

    /**
     * ���X�|���X���s�f�[�^��o�^����
     * @param responseIssue ���X�|���X
     */
    public void setResponseIssue(DataResponseIssue responseIssue) {
        this.responseIssue = responseIssue;
    }

    /**
     * ���X�|���X��������擾����
     * @return ���X�|���X�����
     */
    public GregorianCalendar getDataResponseDate() {
        return this.dataResponseDate;
    }

    /**
     * ���X�|���X�������o�^����
     * @param dataResponseDate ���X�|���X�����
     */
    public void setDataResponseDate(GregorianCalendar dataResponseDate) {
        this.dataResponseDate = dataResponseDate;
    }

    /**
     * DataResponseEffect����f�[�^���Z�b�g����B
     * @param dre DataResponseEffect
     */
    public void setData(DataResponseEffect dre) {
        this.setSlipNo(dre.getSlipNo());
        this.setResponse(dre.getResponse());
        this.setShopID(dre.getShopID());
        this.setResponseIssue(dre.getResponseIssue());
        this.setDataResponseDate(dre.getDataResponseDate());
    }

    /**
     * ResultSetWrapper����f�[�^���Z�b�g����B
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setSlipNo(rs.getInt("slip_no"));
        MstResponse mr = new MstResponse();
        mr.setData(rs);
        this.setResponse(mr);
        this.setShopID(rs.getInt("shop_id"));
        DataResponseIssue mri = new DataResponseIssue();
        mri.setData(rs);
        this.setResponseIssue(mri);
        this.setDataResponseDate(rs.getGregorianCalendar("data_response_date"));
    }

    /**
     * ���X�|���X�f�[�^��o�^����B
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        String sql = "";
        
        // ���X�|���X�����݂��Ȃ��ꍇ�ɂ͏������s��Ȃ킸�A�����Ƃ��ďI���B
        if (this.getResponse() == null || this.getResponse().getResponseID() <= 0) {
            return true;
        }

        // ���R�[�h�̗L�����m�F���A��Ԃɉ�����SQL���擾����
        if (isExists(con)) {
            // ���X�|���X�����݂��Ȃ��ꍇ�ɂ͍폜SQL�����s
            if (this.getResponse().getResponseID() <= 0) {
                sql = this.getDeleteSQL();
            } else {
                sql = this.getUpdateSQL();
            }
        } else {
            sql = this.getInsertSQL();
        }
        // SQL�̎��s���s��
        if (con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean deleteAll(ConnectionWrapper con, Integer shopID, Integer slipNo) {
        boolean result = false;

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update data_response_effect");
        sql.append(" set");
        sql.append("      update_date = current_timestamp");
        sql.append("     ,delete_date = current_timestamp");
        sql.append(" where");
        sql.append("         shop_id = " + SQLUtil.convertForSQL(shopID));
        sql.append("     and slip_no = " + SQLUtil.convertForSQL(slipNo));

        try {

            con.executeUpdate(sql.toString());
            result = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
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
        //Edit start 2103-11-01 Hoa
        if (con == null || this.getResponse() == null) {
            return false;
        }
        //Edit start 2103-11-01 Hoa
        ResultSetWrapper rs = con.executeQuery(this.getExistsSelectSQL());
        return rs.next();
    }

    /**
     * ���X�|���X�f�[�^���ݎ擾SQL
     */
    public String getExistsSelectSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     data_response_effect as dre");
        sql.append(" where");
        sql.append("         dre.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        sql.append("     AND dre.slip_no = coalesce( " + SQLUtil.convertForSQL(this.getSlipNo()) + ", 0 )");
        sql.append("     AND dre.response_id = " + SQLUtil.convertForSQL(this.getResponse().getResponseID()));

        return sql.toString();
    }

    public String getInsertSQL() {
        return "insert into data_response_effect\n"
                + "(slip_no, response_id, response_issue_id, data_response_date, shop_id, insert_date, \n"
                + "update_date, delete_date)\n"
                + "values(\n"
                + SQLUtil.convertForSQL(this.getSlipNo()) + ",\n"
                + SQLUtil.convertForSQL(this.getResponse().getResponseID()) + ",\n"
                + (this.getResponseIssue() == null ? "-1" : SQLUtil.convertForSQL(this.getResponseIssue().getResponseIssueId())) + ",\n"
                + SQLUtil.convertForSQL(this.getDataResponseDate()) + ",\n"
                + SQLUtil.convertForSQL(this.getShopID()) + ",\n"
                + "current_timestamp, current_timestamp, null );\n";
    }

    public String getUpdateSQL() {
        return "update\n"
                + "data_response_effect\n"
                + "set\n"
                + "response_issue_id = " + (this.getResponseIssue() == null ? "-1" : SQLUtil.convertForSQL(this.getResponseIssue().getResponseIssueId())) + ", \n"
                + "data_response_date = " + SQLUtil.convertForSQL(this.getDataResponseDate()) + ", \n"
                + "shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + ", \n"
                + "update_date = current_timestamp,\n"
                + "delete_date = null\n"
                + "where\n"
                + "     shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n"
                + " and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + " and response_id = " + SQLUtil.convertForSQL(this.getResponse().getResponseID()) + "\n"
                + ";\n";
    }

    public String getDeleteSQL() {
        return "update data_response_effect\n"
                + "set\n"
                + "update_date = current_timestamp, \n"
                + "delete_date = current_timestamp\n"
                + "where\n"
                + "     shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n"
                + " and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + " and response_id = " + SQLUtil.convertForSQL(this.getResponse().getResponseID()) + "\n"
                + ";\n";
    }
}

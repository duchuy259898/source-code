/*
 * MstStaffClass.java
 *
 * Created on 2006/04/25, 17:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �X�^�b�t�敪�f�[�^
 *
 * @author katagiri
 */
public class MstResponseClass {

    /**
     * ���X�|���X����ID
     */
    private Integer responseClassId = null;
    /**
     * ���X�|���X���ޖ�
     */
    private String responseClassName = "";
    /**
     * �\����
     */
    private Integer displaySeq = null;
    /**
     * �o�^��
     */
    private Timestamp insertDate = null;
    /**
     *
     */
    private Timestamp updateDate = null;
    /**
     *
     */
    private Timestamp deleteDate = null;

    public MstResponseClass() {
    }

    /**
     * ������ɕϊ�����B�i�X�^�b�t�敪���j
     *
     * @return �X�^�b�t�敪��
     */
    public String toString() {
        return this.getResponseClassName();
    }

    /**
     * �X�^�b�t�敪�h�c���擾����B
     *
     * @return �X�^�b�t�敪�h�c
     */
    public Integer getResponseClassId() {
        return responseClassId;
    }

    /**
     * �X�^�b�t�敪�h�c���Z�b�g����B
     *
     * @param responseClassId �X�^�b�t�敪�h�c
     */
    public void setResponseClassId(Integer responseClassId) {
        this.responseClassId = responseClassId;
    }

    /**
     * �X�^�b�t�敪�����擾����B
     *
     * @return �X�^�b�t�敪��
     */
    public String getResponseClassName() {
        return responseClassName;
    }

    /**
     * �X�^�b�t�敪�����Z�b�g����B
     *
     * @param responseClassName �X�^�b�t�敪��
     */
    public void setetResponseClassName(String responseClassName) {
        this.responseClassName = responseClassName;
    }

    /**
     * �\�������擾����B
     *
     * @return �\����
     */
    public Integer getDisplaySeq() {
        return displaySeq;
    }

    /**
     * �\�������Z�b�g����B
     *
     * @param displaySeq �\����
     */
    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }

    public Timestamp getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Timestamp insertDate) {
        this.insertDate = insertDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public Timestamp getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Timestamp deleteDate) {
        this.deleteDate = deleteDate;
    }

    /**
     * �f�[�^���N���A����B
     */
    public void clear() {
        this.setResponseClassId(null);
        this.setetResponseClassName("");
        this.setDisplaySeq(0);
        this.setInsertDate(null);
        this.setUpdateDate(null);
        this.setDeleteDate(null);
    }

    /**
     * �X�^�b�t�敪�}�X�^�f�[�^����f�[�^���Z�b�g����B
     *
     * @param msc �X�^�b�t�敪�}�X�^�f�[�^
     */
    public void setData(MstResponseClass msc) {
        this.setResponseClassId(msc.getResponseClassId());
        this.setetResponseClassName(msc.getResponseClassName());
        this.setDisplaySeq(msc.getDisplaySeq());
        this.setInsertDate(msc.getInsertDate());
        this.setUpdateDate(msc.getUpdateDate());
        this.setDeleteDate(msc.getDeleteDate());
    }

    /**
     * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
       
        this.setResponseClassId(rs.getInt("response_class_id"));
        this.setetResponseClassName(rs.getString("response_class_name"));
        this.setDisplaySeq(rs.getInt("display_seq"));
        this.setInsertDate(rs.getTimestamp("insert_date"));
        this.setUpdateDate(rs.getTimestamp("update_date"));
        this.setDeleteDate(rs.getTimestamp("delete_date"));
    }

    /**
     * �X�^�b�t�敪�}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
     */
    public ArrayList<MstResponseClass> load(ConnectionWrapper con) throws SQLException {
        ArrayList<MstResponseClass> list = new ArrayList<MstResponseClass>();

        ResultSetWrapper rs = con.executeQuery(getSelectAllSQL());

        while (rs.next()) {
            MstResponseClass msc = new MstResponseClass();
            msc.setData(rs);
            list.add(msc);
        }

        rs.close();

        return list;
    }
    
     /**
     * �X�^�b�t�敪�}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
     */
    public ArrayList<MstResponseClass> loadAllResponseClass(ConnectionWrapper con) throws SQLException {
        ArrayList<MstResponseClass> list = new ArrayList<MstResponseClass>();

        ResultSetWrapper rs = con.executeQuery(getAllResponseSQL());

        while (rs.next()) {
            MstResponseClass msc = new MstResponseClass();
            msc.setData(rs);
            list.add(msc);
        }

        rs.close();

        return list;
    }
    
    /**
     * �X�^�b�t�敪�}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
     */
    public ArrayList<MstResponseClass> loadByClassID(ConnectionWrapper con) throws SQLException {
        ArrayList<MstResponseClass> list = new ArrayList<MstResponseClass>();

        ResultSetWrapper rs = con.executeQuery(getSelectSQL());

        while (rs.next()) {
            MstResponseClass msc = new MstResponseClass();
            msc.setData(rs);
            list.add(msc);
        }

        rs.close();

        return list;
    }

    /**
     * �X�^�b�t�敪�}�X�^�Ƀf�[�^��o�^����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean regist(ConnectionWrapper con,
            Integer lastSeq) throws SQLException {
        if (isExists(con)) {
            if (lastSeq != this.getDisplaySeq()) {
                if (con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0) {
                    return false;
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
        }

        return true;
    }

    /**
     * �X�^�b�t�敪�}�X�^����f�[�^���폜����B�i�_���폜�j
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

        if (con.executeUpdate(sql) != 1) {
            return false;
        }

        if (con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0) {
            return false;
        }

        return true;
    }

    /**
     * �X�^�b�t�敪�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getResponseClassId() == null || this.getResponseClassId() < 1) {
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
    private String getSelectAllSQL() {
        String sqlWhere = "";
        sqlWhere += "SELECT r.response_class_id \n ";
        sqlWhere += "FROM  mst_response r  \n ";
        sqlWhere += "INNER JOIN mst_use_response ur\n ";
        sqlWhere += "ON r.response_id = ur.response_id\n ";
        sqlWhere += "AND ur.shop_id =  " + SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID()) + "\n";
        return "select *\n"
                + "from mst_response_class\n"
                + "where response_class_id IN ( "
                + sqlWhere + ")\n"
                + "AND delete_date is NULL\n"
                + "order by display_seq\n";
    }
    
     /**
     * Select�����擾����B
     *
     * @return Select��
     */
        private String getAllResponseSQL() {
               return "select *\n"
                       + "from mst_response_class\n"
                       + "where delete_date is null\n"
                       + "order by display_seq\n";
           }

    /**
     * Select�����擾����B
     *
     * @return Select��
     */
    private String getSelectSQL() {
        return "select *\n"
                + "from mst_response_class\n"
                + "where delete_date is null\n"
                + "and	response_class_id = " + SQLUtil.convertForSQL(this.getResponseClassId()) + "\n";
    }

    /**
     * �\���������炷�r�p�k�����擾����
     *
     * @param seq ���̕\����
     * @param isIncrement true - ���Z�Afalse - ���Z
     * @return �\���������炷�r�p�k��
     */
    private String getSlideSQL(Integer seq, boolean isIncrement) {
        return "update mst_response_class\n"
                + "set display_seq = display_seq "
                + (isIncrement ? "+" : "-") + " 1\n"
                + "where delete_date is null\n"
                + "and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
    }

    /**
     * Insert�����擾����B
     *
     * @return Insert��
     */
    private String getInsertSQL() {
        return "";
    }

    /**
     * Update�����擾����B
     *
     * @return Update��
     */
    private String getUpdateSQL() {
        return "";
    }

    /**
     * �폜�pUpdate�����擾����B
     *
     * @return �폜�pUpdate��
     */
    private String getDeleteSQL() {
        return "update mst_response_class\n"
                + "set\n"
                + "delete_date = current_timestamp\n"
                + "where	staff_class_id = " + SQLUtil.convertForSQL(this.getResponseClassId()) + "\n";
    }
}

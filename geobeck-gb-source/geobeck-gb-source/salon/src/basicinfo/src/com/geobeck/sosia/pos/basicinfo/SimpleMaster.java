/*
 * SimpleMaster.java
 *
 * Created on 2006/06/05, 13:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo;

import com.geobeck.sosia.pos.master.MstData;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �V���v���}�X�^�o�^���� �i�h�c�A���́A�\�����̂݁j
 *
 * @author katagiri
 */
public class SimpleMaster extends ArrayList<MstData> {

    /**
     * �}�X�^�̖���
     */
    protected String masterName = "";
    /**
     * �e�[�u����
     */
    protected String tableName = "";
    /**
     * �h�c�̗�
     */
    protected String idColName = "";
    /**
     * ���̗̂�
     */
    protected String nameColName = "";
    /**
     * ���̂̍ő啶����
     */
    protected int nameLength = 0;

    /**
     * �R���X�g���N�^
     *
     * @param masterName �}�X�^�̖���
     * @param tableName �e�[�u����
     * @param idColName �h�c�̗�
     * @param nameColName ���̗̂�
     */
    public SimpleMaster(String masterName,
            String tableName,
            String idColName,
            String nameColName,
            int nameLength) {
        this.setMasterName(masterName);
        this.setTableName(tableName);
        this.setIDColName(idColName);
        this.setNameColName(nameColName);
        this.setNameLength(nameLength);
    }

    /**
     * �}�X�^�̖���
     *
     * @return �}�X�^�̖���
     */
    public String getMasterName() {
        return masterName;
    }

    /**
     * �}�X�^�̖���
     *
     * @param masterName �}�X�^�̖���
     */
    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    /**
     * �e�[�u����
     *
     * @return �e�[�u����
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * �e�[�u����
     *
     * @param tableName �e�[�u����
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * �h�c�̗�
     *
     * @return �h�c�̗�
     */
    public String getIDColName() {
        return idColName;
    }

    /**
     * �h�c�̗�
     *
     * @param idColName �h�c�̗�
     */
    public void setIDColName(String idColName) {
        this.idColName = idColName;
    }

    /**
     * ���̗̂�
     *
     * @return ���̗̂�
     */
    public String getNameColName() {
        return nameColName;
    }

    /**
     * ���̗̂�
     *
     * @param nameColName ���̗̂�
     */
    public void setNameColName(String nameColName) {
        this.nameColName = nameColName;
    }

    /**
     * ���̂̍ő啶����
     *
     * @return ���̂̍ő啶����
     */
    public int getNameLength() {
        return nameLength;
    }

    /**
     * ���̂̍ő啶����
     *
     * @param nameLength ���̂̍ő啶����
     */
    public void setNameLength(int nameLength) {
        this.nameLength = nameLength;
    }

    /**
     * �f�[�^���f�[�^�x�[�X����ǂݍ��ށB
     */
    public void loadData() {
        this.clear();

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            ResultSetWrapper rs = con.executeQuery(this.getSelectAllSQL());

            while (rs.next()) {
                MstData temp = new MstData(
                        rs.getInt("data_id"),
                        rs.getString("data_name"),
                        rs.getInt("display_seq"));
                this.add(temp);
            }

            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    public void loadDataForResponse() {
        this.clear();

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            ResultSetWrapper rs = con.executeQuery(this.getSelectAllResponseSQL());

            while (rs.next()) {
                MstData temp = new MstData(
                        rs.getInt("data_id"),
                        rs.getString("data_name"),
                        rs.getInt("display_seq"),
                        rs.getString("response_no"),
                        rs.getInt("response_class_id"),
                        rs.getString("response_class_name"));
                this.add(temp);
            }

            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    private String getSelectAllResponseSQL() {
        String sql = "select response_id as data_id,\n"
                + "response_name  as data_name,\n"
                + "a.response_no,\n"
                + "(case when b.delete_date is null\n"
                + " then a.response_class_id\n"
                + " else 0 end) as response_class_id,\n"
                + " (case when b.delete_date is null\n"
                + " then b.response_class_name\n"
                + " else null end) as response_class_name,\n"
                + "a.display_seq\n"
                + "from mst_response as a\n"
                + "left join mst_response_class as b\n"
                + "on a.response_class_id = b.response_class_id\n"
                + "where a.delete_date is null\n"
                + "order by a.display_seq\n";
        return sql;
    }

    public boolean registForResponse(MstData data, Integer dataIndex) {
        boolean result = false;

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            con.begin();

            if (this.registDataForResponse(con, data, dataIndex)) {
                con.commit();
                result = true;
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    private boolean registDataForResponse(ConnectionWrapper con,
            MstData data, Integer dataIndex) throws SQLException {
        //���݂��Ȃ��f�[�^�̏ꍇ
        if (dataIndex < 0) {
            //�\���������͂���Ă���ꍇ
            if (0 <= data.getDisplaySeq()) {
                //���͂��ꂽ�\�����ȍ~�̃f�[�^�̕\�������P���Z����
                if (con.executeUpdate(this.getSlideSQL(data.getDisplaySeq(), true)) < 0) {
                    return false;
                }
            }
            //�f�[�^��Insert����
            if (con.executeUpdate(this.getInsertDataSQLForResponse(data)) != 1) {
                return false;
            }
        } //���݂���f�[�^�̏ꍇ
        else {
            //�\�������ύX����Ă���ꍇ
            if (this.get(dataIndex).getDisplaySeq() != data.getDisplaySeq()) {
                //���̕\�����ȍ~�̃f�[�^�̕\�������P���Z����
                if (con.executeUpdate(this.getSlideSQL(this.get(dataIndex).getDisplaySeq(), false)) < 0) {
                    return false;
                }
                //�\���������͂���Ă���ꍇ
                if (0 <= data.getDisplaySeq()) {
                    //���͂��ꂽ�\�����ȍ~�̃f�[�^�̕\�������P���Z����
                    if (con.executeUpdate(this.getSlideSQL(data.getDisplaySeq(), true)) < 0) {
                        return false;
                    }
                }
            }
            //�f�[�^��Update����
            if (con.executeUpdate(this.getUpdateDataSQLForResponse(data)) != 1) {
                return false;
            }
        }

        return true;
    }

    private String getInsertDataSQLForResponse(MstData data) {
        return "insert into " + this.getTableName() + "\n"
                + "(" + this.idColName + ", " + this.nameColName + ", \n" + "response_no, response_class_id, display_seq, "
                + "insert_date, update_date, delete_date)"
                + "select\n"
                + "coalesce(max(" + this.getIDColName() + "), 0) + 1,\n"
                + SQLUtil.convertForSQL(data.getName()) + ",\n"
                + SQLUtil.convertForSQL(data.getResponse_no()) + ",\n"
                + SQLUtil.convertForSQL(data.getResponse_class_id()) + ",\n"
                + "case\n"
                + "when " + SQLUtil.convertForSQL(data.getDisplaySeq())
                + " between 1 and coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null), 0) then "
                + SQLUtil.convertForSQL(data.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null), 0) + 1 end,\n"
                + "current_timestamp, current_timestamp, null\n"
                + "from " + this.getTableName() + "\n";
    }

    private String getUpdateDataSQLForResponse(MstData data) {
        String sql = "update " + this.getTableName() + "\n"
                + "set\n"
                + this.getNameColName() + " = "
                + SQLUtil.convertForSQL(data.getName()) + ",\n"
                + "response_no = " + SQLUtil.convertForSQL(data.getResponse_no()) + ",\n"
                + "response_class_id = " + SQLUtil.convertForSQL(data.getResponse_class_id()) + ",\n"
                + "display_seq = case\n"
                + "when " + SQLUtil.convertForSQL(data.getDisplaySeq())
                + " between 1 and coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null\n"
                + "and " + this.getIDColName() + " != "
                + SQLUtil.convertForSQL(data.getID()) + "), 0) then "
                + SQLUtil.convertForSQL(data.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null\n"
                + "and " + this.getIDColName() + " != "
                + SQLUtil.convertForSQL(data.getID()) + "), 0) + 1 end,\n"
                + "update_date = current_timestamp\n"
                + "where " + this.getIDColName() + " = "
                + SQLUtil.convertForSQL(data.getID()) + "\n";

        return sql;
    }

    /**
     * �S�Ẵf�[�^���擾����r�p�k�����擾����B
     *
     * @return �S�Ẵf�[�^���擾����r�p�k��
     */
    private String getSelectAllSQL() {
        return "select " + this.getIDColName() + " as data_id,\n"
                + this.getNameColName() + " as data_name,\n"
                + "display_seq\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null\n"
                + "order by display_seq\n";
    }

    /**
     * �f�[�^��o�^����B
     *
     * @param data �ΏۂƂȂ�f�[�^
     * @param dataIndex �f�[�^�̃C���f�b�N�X
     * @return true - ����
     */
    public boolean regist(MstData data, Integer dataIndex) {
        boolean result = false;

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            con.begin();

            if (this.registData(con, data, dataIndex)) {
                con.commit();
                result = true;
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    /**
     * �f�[�^��o�^����B
     *
     * @param con ConnectionWrapper
     * @param data �ΏۂƂȂ�f�[�^
     * @param dataIndex �f�[�^�̃C���f�b�N�X
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    private boolean registData(ConnectionWrapper con,
            MstData data, Integer dataIndex) throws SQLException {
        //���݂��Ȃ��f�[�^�̏ꍇ
        if (dataIndex < 0) {
            //�\���������͂���Ă���ꍇ
            if (0 <= data.getDisplaySeq()) {
                //���͂��ꂽ�\�����ȍ~�̃f�[�^�̕\�������P���Z����
                if (con.executeUpdate(this.getSlideSQL(data.getDisplaySeq(), true)) < 0) {
                    return false;
                }
            }
            //�f�[�^��Insert����
           
            
            if (con.executeUpdate(this.getInsertDataSQL(data)) != 1) {
                return false;
            }
        } //���݂���f�[�^�̏ꍇ
        else {
            //�\�������ύX����Ă���ꍇ
            if (this.get(dataIndex).getDisplaySeq() != data.getDisplaySeq()) {
                //���̕\�����ȍ~�̃f�[�^�̕\�������P���Z����
                if (con.executeUpdate(this.getSlideSQL(this.get(dataIndex).getDisplaySeq(), false)) < 0) {
                    return false;
                }
                //�\���������͂���Ă���ꍇ
                if (0 <= data.getDisplaySeq()) {
                    //���͂��ꂽ�\�����ȍ~�̃f�[�^�̕\�������P���Z����
                    if (con.executeUpdate(this.getSlideSQL(data.getDisplaySeq(), true)) < 0) {
                        return false;
                    }
                }
            }
            //�f�[�^��Update����
            if (con.executeUpdate(this.getUpdateDataSQL(data)) != 1) {
                return false;
            }
        }

        return true;
    }

    /**
     * �f�[�^���폜����B
     *
     * @param data �ΏۂƂȂ�f�[�^
     * @return true - ����
     */
    public boolean delete(MstData data) {
        boolean result = false;

        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            con.begin();

            if (this.deleteData(con, data)) {
                con.commit();
                result = true;
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    /**
     * �f�[�^���폜����B
     *
     * @param con ConnectionWrapper
     * @param data �ΏۂƂȂ�f�[�^
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    private boolean deleteData(ConnectionWrapper con,
            MstData data) throws SQLException {
        if (con.executeUpdate(this.getSlideSQL(data.getDisplaySeq(), false)) < 0) {
            return false;
        }

        if (con.executeUpdate(this.getDeleteDataSQL(data)) != 1) {
            return false;
        }

        return true;
    }

    /**
     * �\���������炷�r�p�k�����擾����B
     *
     * @param seq �\����
     * @param isIncrement true - ���₷
     * @return �\���������炷�r�p�k��
     */
    private String getSlideSQL(Integer seq, boolean isIncrement) {
        return "update " + this.getTableName() + "\n"
                + "set display_seq = display_seq "
                + (isIncrement ? "+" : "-") + " 1\n"
                + "where delete_date is null\n"
                + "and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
    }

    /**
     * �f�[�^��Insert����r�p�k�����擾����B
     *
     * @param data �ΏۂƂȂ�f�[�^
     * @return �f�[�^��Insert����r�p�k��
     */
    private String getInsertDataSQL(MstData data) {
        return "insert into " + this.getTableName() + "\n"
                + "(" + this.idColName + ", " + this.nameColName + ", \n" + "display_seq, "
                + "insert_date, update_date, delete_date)"
                + "select\n"
                + "coalesce(max(" + this.getIDColName() + "), 0) + 1,\n"
                + SQLUtil.convertForSQL(data.getName()) + ",\n"
                + "case\n"
                + "when " + SQLUtil.convertForSQL(data.getDisplaySeq())
                + " between 1 and coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null), 0) then "
                + SQLUtil.convertForSQL(data.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null), 0) + 1 end,\n"
                + "current_timestamp, current_timestamp, null\n"
                + "from " + this.getTableName() + "\n";
    }

    /**
     * �f�[�^��Update����r�p�k�����擾����B
     *
     * @param data �ΏۂƂȂ�f�[�^
     * @return �f�[�^��Update����r�p�k��
     */
    private String getUpdateDataSQL(MstData data) {
        return "update " + this.getTableName() + "\n"
                + "set\n"
                + this.getNameColName() + " = "
                + SQLUtil.convertForSQL(data.getName()) + ",\n"
                + "display_seq = case\n"
                + "when " + SQLUtil.convertForSQL(data.getDisplaySeq())
                + " between 1 and coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null\n"
                + "and " + this.getIDColName() + " != "
                + SQLUtil.convertForSQL(data.getID()) + "), 0) then "
                + SQLUtil.convertForSQL(data.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from " + this.getTableName() + "\n"
                + "where delete_date is null\n"
                + "and " + this.getIDColName() + " != "
                + SQLUtil.convertForSQL(data.getID()) + "), 0) + 1 end,\n"
                + "update_date = current_timestamp\n"
                + "where " + this.getIDColName() + " = "
                + SQLUtil.convertForSQL(data.getID()) + "\n";
    }

    /**
     * �f�[�^���폜�i�_���폜�j����r�p�k�����擾����B
     *
     * @param data �ΏۂƂȂ�f�[�^
     * @return �f�[�^���폜�i�_���폜�j����r�p�k��
     */
    //IVS_LTThuc start add 20140805 MASHU_�Ƒԓo�^
    private String getDeleteDataSQL(MstData data) {
        return "update " + this.getTableName() + "\n"
                + "set\n"
                + "update_date = current_timestamp,\n"
                + "delete_date = current_timestamp\n"
                + "where " + this.getIDColName() + " = " + SQLUtil.convertForSQL(data.getID()) + "\n";
    }
    public int getDeleteDataSQL2(String table,MstData data){
        String sql = "select count(shop_category_id) as number from "+table 
                +" where shop_category_id = " +SQLUtil.convertForSQL(data.getID())
                +" and delete_date is  null";
         ConnectionWrapper con = SystemInfo.getConnection();
        int count=0;
        try {
            ResultSetWrapper rs = con.executeQuery(sql);
           if(rs.next()){
           count = rs.getInt("number");
           }
            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    return count;
        
    }
 //IVS_LTThuc end add 20140805 MASHU_�Ƒԓo�^
}

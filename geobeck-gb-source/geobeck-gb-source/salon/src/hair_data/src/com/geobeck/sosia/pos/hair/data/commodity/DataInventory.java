/*
 * DataInventory.java
 *
 * Created on 2008/09/19, 19:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.commodity;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mizukawa
 */
public class DataInventory {

    /** �I��ID */
    private Integer inventoryId;
    /** �I���敪 */
    private Integer inventory_division;
    /** �V���b�vID */
    private Integer shopId;
    /** �I���I���� */
    private Date inventoryDate;
    /** �m��t���O */
    private Integer fixed;
    /** �ŏI�X�V�� */
    private Date updateDate;
    /** �I���ڍ� */
    private List<DataInventoryDetail> detail = new ArrayList<DataInventoryDetail>();

    /** Creates a new instance of DataInventory */
    public DataInventory() {
    }

    /**
     * �I��ID��Ԃ��B
     * @return �I��ID
     */
    public Integer getInventoryId() {
        return this.inventoryId;
    }

    /**
     * �I��ID���Z�b�g����B
     * @param v �I��ID
     */
    public void setInventoryId(Integer v) {
        this.inventoryId = v;
    }

    /**
     * �I��ID��Ԃ��B
     * @return �I��ID
     */
    public Integer getInventoryDivision() {
        return this.inventory_division;
    }

    /**
     * �I��ID���Z�b�g����B
     * @param v �I��ID
     */
    public void setInventoryDivision(Integer v) {
        this.inventory_division = v;
    }

    /**
     * �V���b�vID��Ԃ��B
     * @return �V���b�vID
     */
    public Integer getShopId() {
        return this.shopId;
    }

    /**
     * �V���b�vID���Z�b�g����B
     * @param v �V���b�vID
     */
    public void setShopId(Integer v) {
        this.shopId = v;
    }

    /**
     * �I���I������Ԃ��B
     * @return �I���I����
     */
    public Date getInventoryDate() {
        return this.inventoryDate;
    }

    /**
     * �I���I�������Z�b�g����B
     * @param v �I���I����
     */
    public void setInventoryDate(Date v) {
        this.inventoryDate = v;
    }

    /**
     * �m��t���O��Ԃ��B
     * @return �m��t���O
     */
    public Integer getFixed() {
        return this.fixed;
    }

    /**
     * �m��t���O���Z�b�g����B
     * @param v �m��t���O
     */
    public void setFixed(Integer v) {
        this.fixed = v;
    }

    /**
     * �ŏI�X�V��
     * @param updateDate �ŏI�X�V��
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * �ŏI�X�V��
     * @return �ŏI�X�V��
     */
    public Date getUpdateDate() {
        return this.updateDate;
    }

    /**
     * ���݊m�F�pSQL�����擾����B
     * @return SQL��
     */
    private String getExistSQL(boolean lock) {
        StringBuilder buf = new StringBuilder();
        if (this.getInventoryId() == null) {
            buf.append("select\n").append("  inventory_id\n").append("from\n").append("  data_inventory\n").append("where\n").append("  shop_id = ").append(SQLUtil.convertForSQL(this.getShopId())).append("\n").append("and\n").append("  inventory_date = ").append(SQLUtil.convertForSQLDateOnly(this.getInventoryDate())).append("\n");
        } else {
            buf.append("SELECT\n").append("  inventory_id\n").append("FROM\n").append("  data_inventory\n").append("WHERE\n").append("  inventory_id = ").append(SQLUtil.convertForSQL(this.getInventoryId())).append("\n").append("AND\n").append("  inventory_division = ").append(SQLUtil.convertForSQL(this.getInventoryDivision())).append("\n");
        }

        if (lock) {
            buf.append("for update\n");
        }

        return new String(buf);
    }

    /**
     * �V�K�I��ID�̔ԗpSQL���擾����B
     * @return ����SQL��
     */
    private String getNewInventryIDSQL() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" SELECT ");
        sql.append("     COALESCE(MAX(inventory_id),0) + 1 AS new_inventory_id ");
        sql.append(" FROM  data_inventory ");

        return sql.toString();
    }

    /**
     * Insert�����擾����B
     * @return Insert��
     */
    private String getInsertSQL() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" INSERT INTO data_inventory ( ");
        sql.append("     inventory_id ");
        sql.append("    ,inventory_division ");
        sql.append("    ,shop_id ");
        sql.append("    ,inventory_date ");
        sql.append("    ,fixed ");
        sql.append("    ,insert_date ");
        sql.append("    ,update_date ");
        sql.append("    ,delete_date ");
        sql.append(" ) VALUES ( ");
        sql.append("  " + SQLUtil.convertForSQL(this.getInventoryId()));
        sql.append(" ," + SQLUtil.convertForSQL(this.getInventoryDivision()));
        sql.append(" ," + SQLUtil.convertForSQL(this.getShopId()));
        sql.append(" ," + SQLUtil.convertForSQLDateOnly(this.getInventoryDate()));
        sql.append(" ," + SQLUtil.convertForSQL(this.getFixed()));
        sql.append(" ,current_timestamp ");
        sql.append(" ,current_timestamp ");
        sql.append(" ,NULL ");
        sql.append(" ) ");

        return sql.toString();
    }

    /**
     * Update�����擾����B
     * @return Update��
     */
    private String getUpdateSQL() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" UPDATE data_inventory ");
        sql.append(" SET ");
        sql.append("     shop_id = " + SQLUtil.convertForSQL(this.getShopId()));
        sql.append("    ,inventory_date = " + SQLUtil.convertForSQLDateOnly(this.getInventoryDate()));
        sql.append("    ,fixed = " + SQLUtil.convertForSQL(this.getFixed()));
        sql.append("    ,update_date = current_timestamp ");
        sql.append(" WHERE ");
        sql.append("     inventory_id = " + SQLUtil.convertForSQL(this.getInventoryId()));
        sql.append(" AND ");
        sql.append("     inventory_division = " + SQLUtil.convertForSQL(this.getInventoryDivision()));

        return sql.toString();
    }

    /**
     * ���̃I�u�W�F�N�g�Ƀf�[�^�x�[�X����擾�����l���Z�b�g����B
     * @param rs �₢���킹����
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        setInventoryId(rs.getInt("inventory_id"));
        setInventoryDivision(rs.getInt("inventory_division"));
        setShopId(rs.getInt("shop_id"));
        setInventoryDate(rs.getTimestamp("inventory_date"));
        setFixed(rs.getInt("fixed"));
        setUpdateDate(rs.getTimestamp("update_date"));
    }

    /**
     * �V�����I��ID���Z�b�g����B
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setNewInventoryID(ConnectionWrapper con) throws SQLException {
        Integer invId = matchInventory(con);
        if (invId != null) {
            this.setInventoryId(invId);
        } else {
            ResultSetWrapper rs = con.executeQuery(this.getNewInventryIDSQL());

            if (rs.next()) {
                this.setInventoryId(rs.getInt("new_inventory_id"));
            }

            rs.close();
        }
    }

    /**
     * �I���Ƀf�[�^�����݂��邩�`�F�b�N����B
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getExistSQL(true));

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * �I���ɓX�܁A�I���I�����Ƀ}�b�`����f�[�^�����݂��邩�`�F�b�N���A���݂����炻�̒I��ID��Ԃ�
     * @param con �f�[�^�x�[�X�R�l�N�V����
     * @return �I��ID/null
     */
    public Integer matchInventory(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(this.getExistSQL(true));

        if (rs.next()) {
            return rs.getInt("inventory_id");
        } else {
            return null;
        }
    }

    /**
     * �ǉ�
     */
    public boolean insert(ConnectionWrapper con) throws SQLException {
        if (con.executeUpdate(this.getInsertSQL()) < 0) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * �X�V
     */
    public boolean update(ConnectionWrapper con) throws SQLException {
        if (con.executeUpdate(this.getUpdateSQL()) < 0) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * ���̃��R�[�h���N���ɍX�V���ꂽ���ǂ�����Ԃ�
     */
    public boolean isUpdated(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(this.getCheckUpdateSQL());
        try {
            if (rs.next()) {
                Date dt = rs.getTimestamp("update_date");
                return !dt.equals(getUpdateDate());
            } else {
                if (getUpdateDate() == null) {
                    return false;
                }

                return true;
            }
        } finally {
            rs.close();
        }
    }

    private String getCheckUpdateSQL() {
        StringBuilder buf = new StringBuilder();
        if (getInventoryId() != null) {
            buf.append("select\n").append("  update_date\n").append("from\n").append("  data_inventory\n").append("where\n").append("  inventory_id = ").append(SQLUtil.convertForSQL(getInventoryId())).append("\n").append("and\n").append("  inventory_division = ").append(SQLUtil.convertForSQL(getInventoryDivision())).append("\n");
        } else {
            buf.append("select\n").append("  update_date\n").append("from\n").append("  data_inventory\n").append("where\n").append("  shop_id = ").append(SQLUtil.convertForSQL(getShopId())).append("\n").append("and\n").append("  inventory_date = ").append(SQLUtil.convertForSQLDateOnly(getInventoryDate())).append("\n").append("and\n").append("  delete_date is null\n");
        }

        return new String(buf);
    }

    /**
     * �ŐV�̒I���f�[�^���f�[�^�x�[�X����擾�����̃I�u�W�F�N�g�ɒl���Z�b�g����B
     * @param con �f�[�^�x�[�X�R�l�N�V����
     * @param shopId �V���b�vID
     */
    public void loadLatest(ConnectionWrapper con, Integer shopId, Date basedate) throws SQLException {
        // �ŐV�̒I���f�[�^���擾����
        ResultSetWrapper rs = con.executeQuery(getLoadLatestSQL(shopId, basedate));
        try {
            if (rs.next()) {
                this.setData(rs);
            }
        } finally {
            rs.close();
        }

        // �ŐV�̒I���f�[�^�ɕR�t���ڍ׃f�[�^���擾����
        rs = con.executeQuery(getDetailSQL(this.getInventoryId(), this.getInventoryDivision()));
        try {
            detail.clear();
            while (rs.next()) {
                DataInventoryDetail d = new DataInventoryDetail();
                d.setData(rs);
                detail.add(d);
            }
        } finally {
            rs.close();
        }
    }

    /**
     * �ŐV�̒I���f�[�^���f�[�^�x�[�X����擾���邽�߂�SQL��Ԃ��B
     * @param shopId �V���b�vID
     * @return �ŐV�̒I���f�[�^���f�[�^�x�[�X����擾���邽�߂�SQL
     */
    public String getLoadLatestSQL(Integer shopId, Date basedate) {
        StringBuilder buf = new StringBuilder();
        buf.append("select d.* from data_inventory d\n").append("where\n").append("shop_id = ").append(SQLUtil.convertForSQL(shopId)).append("\n").append("and d.inventory_date = (\n").append("select max(inventory_date) from (\n").append("select max(inventory_date) as inventory_date from data_inventory where shop_id = ").append(SQLUtil.convertForSQL(shopId)).append(" and inventory_date <= ").append(SQLUtil.convertForSQLDateOnly(basedate)).append(" and delete_date is null\n").append("union\n").append("select min(inventory_date) as inventory_date from data_inventory where shop_id = ").append(SQLUtil.convertForSQL(shopId)).append(" and inventory_date >= ").append(SQLUtil.convertForSQLDateOnly(basedate)).append(" and delete_date is null\n").append(") a\n").append(")");
        return new String(buf);
    }

    /**
     * �I�����̏ڍׂ��擾���邽�߂�SQL��Ԃ��B
     * @param inventoryId �I��ID
     * @return �I�����̏ڍׂ��擾���邽�߂�SQL
     */
    public String getDetailSQL(Integer inventoryId, Integer inventory_division) {
        StringBuilder buf = new StringBuilder();
        buf.append("select d.*, mi.* from data_inventory_detail d, mst_item mi where d.item_id = mi.item_id and d.delete_date is null and inventory_id = ").append(SQLUtil.convertForSQL(inventoryId));
        buf.append(" and inventory_division = ").append(SQLUtil.convertForSQL(inventory_division));
        return new String(buf);
    }

    /**
     *
     */
    public DataInventoryDetail getDetail(Integer itemId, Integer inventoryDivision) {
        for (DataInventoryDetail d : detail) {
            if (itemId.equals(d.getItemID())) {
                if (inventoryDivision.equals(d.getInventoryDivision())) {
                    return d;
                }
            }
        }

        return null;
    }

    /*
     * �I�����׃f�[�^�̒ǉ�
     */
    public void addDetail(DataInventoryDetail dd) {
        detail.add(dd);
    }
}

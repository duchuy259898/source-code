/*
 * DataInventoryDetail.java
 *
 * Created on 2008/09/19, 19:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.commodity;

import com.geobeck.sosia.pos.master.product.MstItem;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 *
 * @author e_mizukawa
 */
public class DataInventoryDetail extends MstItem {

    private Integer inventoryID;
    private Integer itemID;
    private Integer inventoryDivision;
    private Integer initialStock;
    private Integer realStock;
    private Long costPrice;

    /** Creates a new instance of DataInventoryDetail */
    public DataInventoryDetail() {
    }

    public Integer getInventoryID() {
        return this.inventoryID;
    }

    public void setInventoryID(Integer v) {
        this.inventoryID = v;
    }

    public Integer getItemID() {
        return this.itemID;
    }

    public void setItemID(Integer v) {
        this.itemID = v;
    }

    public Integer getInventoryDivision() {
        return this.inventoryDivision;
    }

    public void setInventoryDivision(Integer v) {
        this.inventoryDivision = v;
    }

    public void setInitialStock(Integer v) {
        this.initialStock = v;
    }

    public Integer getInitialStock() {
        return this.initialStock;
    }

    public Integer getRealStock() {
        return this.realStock;
    }

    public void setRealStock(Integer v) {
        this.realStock = v;
    }

    public Long getCostPrice() {
        return this.costPrice;
    }

    public void setCostPrice(Long v) {
        this.costPrice = v;
    }

    public void setData(ResultSetWrapper rs) throws SQLException {
        super.setData(rs);
        setInventoryID(rs.getInt("inventory_id"));
        setItemID(rs.getInt("item_id"));
        setInventoryDivision(rs.getInt("inventory_division"));
        setInitialStock(rs.getInt("initial_stock"));
        setRealStock(rs.getInt("real_stock"));
        setCostPrice(rs.getLong("cost_price"));
    }

    /**
     * ���݊m�F�pSQL�����擾����B
     * @return SQL��
     */
    private String getExistSQL() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" SELECT\n");
        sql.append("     item_id\n");
        sql.append(" FROM\n");
        sql.append("     data_inventory_detail\n");
        sql.append(" WHERE\n");
        sql.append("     inventory_id = " + SQLUtil.convertForSQL(this.getInventoryID()) + "\n");
        sql.append(" AND\n");
        sql.append("     item_id = " + SQLUtil.convertForSQL(this.getItemID()) + "\n");
        sql.append(" AND\n");
        sql.append("     inventory_division = " + SQLUtil.convertForSQL(this.getInventoryDivision()) + "\n");

        return sql.toString();
    }

    /**
     * Insert�����擾����B
     * @return Insert��
     */
    private String getInsertSQL() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" INSERT INTO data_inventory_detail (\n");
        sql.append("     inventory_id\n");
        sql.append("    ,item_id\n");
        sql.append("    ,inventory_division\n");
        sql.append("    ,initial_stock\n");
        sql.append("    ,real_stock\n");
        sql.append("    ,cost_price\n");
        sql.append("    ,insert_date\n");
        sql.append("    ,update_date\n");
        sql.append("    ,delete_date\n");
        sql.append(" ) VALUES (\n");
        sql.append("  " + SQLUtil.convertForSQL(this.getInventoryID()) + "\n");
        sql.append(" ," + SQLUtil.convertForSQL(this.getItemID()) + "\n");
        sql.append(" ," + SQLUtil.convertForSQL(this.getInventoryDivision()) + "\n");
        sql.append(" ," + SQLUtil.convertForSQL(this.getInitialStock()) + "\n");
        sql.append(" ," + SQLUtil.convertForSQL(this.getRealStock()) + "\n");
        sql.append(" ," + SQLUtil.convertForSQL(this.getCostPrice()) + "\n");
        sql.append(" ,current_timestamp\n");
        sql.append(" ,current_timestamp\n");
        sql.append(" ,NULL\n");
        sql.append(" )\n");

        return sql.toString();
    }

    /**
     * Update�����擾����B
     * @return Update��
     */
    private String getUpdateSQL() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" UPDATE data_inventory_detail\n");
        sql.append(" SET\n");
        sql.append("     initial_stock = " + SQLUtil.convertForSQL(this.getInitialStock()) + "\n");
        sql.append("    ,real_stock = " + SQLUtil.convertForSQL(this.getRealStock()) + "\n");
        sql.append("    ,cost_price = " + SQLUtil.convertForSQL(this.getCostPrice()) + "\n");
        sql.append("    ,update_date = current_timestamp\n");
        sql.append(" WHERE\n");
        sql.append("     inventory_id = " + SQLUtil.convertForSQL(this.getInventoryID()) + "\n");
        sql.append(" AND\n");
        sql.append("     item_id = " + SQLUtil.convertForSQL(this.getItemID()) + "\n");
        sql.append(" AND\n");
        sql.append("     inventory_division = " + SQLUtil.convertForSQL(this.getInventoryDivision()) + "\n");

        return sql.toString();
    }

    /**
     * �����폜SQL���擾����B
     * @param inventoryId �I��ID
     * @return �����폜SQL
     */
    private static String getPhysicalDeleteSQL(int inventoryId, int inventoryDivision) {
        return "delete from data_inventory_detail\n"
                + "where inventory_id = " + SQLUtil.convertForSQL(inventoryId) + "\n"
                + "and inventory_division = " + SQLUtil.convertForSQL(inventoryDivision) + "\n";
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

        ResultSetWrapper rs = con.executeQuery(this.getExistSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
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
     * �I��ID�ɕR�t�����ׂĂ̖��ׂ𕨗��폜����B
     * @param con �f�[�^�x�[�X�R�l�N�V����
     * @param inventoryId �I��ID
     */
    public static void physicalDelete(ConnectionWrapper con, int inventoryId, int inventoryDivision) throws SQLException {
        con.executeUpdate(getPhysicalDeleteSQL(inventoryId, inventoryDivision));
    }
}

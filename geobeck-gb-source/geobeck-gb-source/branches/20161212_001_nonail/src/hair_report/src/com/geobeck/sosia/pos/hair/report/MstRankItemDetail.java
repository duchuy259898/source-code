/*
 * MstUseProducts.java
 *
 * Created on 2007/01/18, 15:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.products.*;
import java.sql.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * @author ttmloan
 */
public class MstRankItemDetail {

    /**
     * �����敪�i1�F�Z�p�A2�F���i�j
     */
    private Integer productDivision = null;

    /**
     * �Q�Ə��i�E�Z�p
     */
    private ProductClasses referenceProducts = new ProductClasses();
    /**
     * �I�����i�E�Z�p
     */
    private ProductClasses selectProducts = new ProductClasses();

    /**
     * �R���X�g���N�^
     */
    public MstRankItemDetail() {
    }

    /**
     * �����敪�i1�F�Z�p�A2�F���i�j���擾����B
     *
     * @return �����敪�i1�F�Z�p�A2�F���i�j
     */
    public Integer getProductDivision() {
        return productDivision;
    }

    /**
     * �����敪�i1�F�Z�p�A2�F���i�j��ݒ肷��B
     *
     * @param productDivision �����敪�i1�F�Z�p�A2�F���i�j
     */
    public void setProductDivision(Integer productDivision) {
        this.productDivision = productDivision;
        referenceProducts.setProductDivision(productDivision);
        selectProducts.setProductDivision(productDivision);
    }

    /**
     * �����敪�����擾����B
     *
     * @return �����敪��
     */
    public String getProductDivisionName() {
        switch (productDivision) {
            case 1:
                return "�X�܎g�p�Z�p";
            case 2:
                return "�X�܎g�p���i";
            default:
                return "";
        }
    }

    /**
     * �Q�Ə��i�E�Z�p���擾����B
     *
     * @return �Q�Ə��i�E�Z�p
     */
    public ProductClasses getReferenceProducts() {
        return referenceProducts;
    }

    /**
     * �Q�Ə��i�E�Z�p��ݒ肷��B
     *
     * @param referenceProducts �Q�Ə��i�E�Z�p
     */
    public void setReferenceProducts(ProductClasses referenceProducts) {
        this.referenceProducts = referenceProducts;
    }

    /**
     * �I�����i�E�Z�p���擾����B
     *
     * @return �I�����i�E�Z�p
     */
    public ProductClasses getSelectProducts() {
        return selectProducts;
    }

    /**
     * �I�����i�E�Z�p��ݒ肷��B
     *
     * @param selectProducts �I�����i�E�Z�p
     */
    public void setSelectProducts(ProductClasses selectProducts) {
        this.selectProducts = selectProducts;
    }

    /**
     * �f�[�^��ǂݍ��ށB
     *
     * @param con ConnectionWrapper
     * @return true - �����Afalse - ���s
     * @throws java.sql.SQLException SQLException
     */
    public boolean loadRankItemDetail(ConnectionWrapper con, Integer itemGroupId) throws SQLException {
        if (!referenceProducts.load(con)) {
            return false;
        }

        if (!selectProducts.load(con)) {
            return false;
        }

        for (ProductClass pc : referenceProducts) {
            if (!pc.loadProductsRankItemDetail(con, this.getProductDivision())) {
                return false;
            }
        }

        if (!this.loadMstUseProductForRankItemDetail(con, itemGroupId)) {
            return false;
        }

        return true;
    }

    /**
     * �戵���i�E�Z�p��ǂݍ��ށB
     *
     * @param con ConnectionWrapper
     * @return true - ����
     * @throws java.sql.SQLException SQLException
     */
    private boolean loadMstUseProductForRankItemDetail(ConnectionWrapper con, Integer itemGroupId) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(this.getLoadMstUseProductForRankItemDetailSQL(itemGroupId));

        while (rs.next()) {
            ProductClass pc = referenceProducts.getProductClass(rs.getInt("product_class_id"));
            if (pc == null) {
                continue;
            }

            Product p = pc.getProduct(rs.getInt("product_id"));
            if (p == null) {
                continue;
            }

            long price = rs.getLong("price");
            if (!rs.wasNull()) {
                p.setPrice(price);
            }
            
            int displaySeq = rs.getInt("display_seq");
            if (rs.wasNull()) {
                p.setDisplaySeq(null);
            } else {
                p.setDisplaySeq(displaySeq);
            }

            this.moveProduct(true,
                    referenceProducts.getProductClassIndex(rs.getInt("product_class_id")),
                    referenceProducts.getProductClass(rs.getInt("product_class_id")).getProductIndex(rs.getInt("product_id")));
        }

        rs.close();
        rs = null;

        return true;
    }

    /**
     * �戵���i�E�Z�p��ǂݍ��ނr�p�k�����擾����B
     *
     * @return �戵���i�E�Z�p��ǂݍ��ނr�p�k��
     */
    private String getLoadMstUseProductForRankItemDetailSQL(Integer itemGroupId) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      md.item_id as product_id");
        sql.append("     ,mi.item_name as product_name");
        sql.append("     ,mi.price");
        sql.append("     ,mi.item_class_id as product_class_id");
        sql.append("     ,mi.display_seq");
        sql.append(" from");
        sql.append("     mst_rank_item_detail md");
        sql.append("     inner join mst_item mi");
        sql.append("                 on mi.item_id = md.item_id");
        sql.append("                 and mi.delete_date is null");
        sql.append(" where");
        sql.append("     md.item_group_id = " + SQLUtil.convertForSQL(itemGroupId));
        sql.append("     and md.delete_date is null");
        sql.append(" order by");
        sql.append("     mi.display_seq");

        return sql.toString();
    }

    /**
     * ���i�E�Z�p��I���i�����j����B
     *
     * @param isSelect true�F�I���Afalse�F����
     * @param productClassIndex ���ނ̃C���f�b�N�X
     * @param productIndex ���i�E�Z�p�̃C���f�b�N�X
     */
    public void moveProduct(boolean isSelect, Integer productClassIndex, Integer productIndex) {
        if (productClassIndex == null || productIndex == null) {
            return;
        }

        ProductClasses from = (isSelect ? referenceProducts : selectProducts);
        ProductClasses to = (isSelect ? selectProducts : referenceProducts);

        ProductClass pcFrom = from.get(productClassIndex);
        ProductClass pcTo = to.get(productClassIndex);

        pcTo.add(pcFrom.remove(productIndex.intValue()));
    }

    /**
     * ���i�E�Z�p��S�đI���i�����j����B
     *
     * @param isSelect true�F�I���Afalse�F����
     * @param productClassIndex ���ނ̃C���f�b�N�X
     */
    public void moveProductAll(boolean isSelect, Integer productClassIndex) {
        if (productClassIndex == null) {
            return;
        }

        ProductClasses from = (isSelect ? referenceProducts : selectProducts);
        ProductClasses to = (isSelect ? selectProducts : referenceProducts);

        ProductClass pcFrom = from.get(productClassIndex);
        ProductClass pcTo = to.get(productClassIndex);

        while (0 < pcFrom.size()) {
            pcTo.add(pcFrom.remove(0));
        }
    }

    public void sort(boolean isSelect, Integer productClassIndex) {
        if (productClassIndex == null) {
            return;
        }

        ProductClasses pcs = (isSelect ? selectProducts : referenceProducts);
        ProductClass pc = pcs.get(productClassIndex);
        pc.sort();
    }

    /**
     * �o�^�������s���B
     *
     * @param con ConnectionWrapper
     * @return true - �����Afalse - ���s
     * @throws java.sql.SQLException SQLException
     */
    public boolean registRankItemDetail(ConnectionWrapper con, Integer itemGroupId) throws SQLException {
        //���f�[�^���폜
        if (!this.deleteDataRankItemDetail(con, itemGroupId)) {
            return false;
        }
        //�f�[�^��o�^
        if (!this.registDataRankItemDetail(con, itemGroupId)) {
            return false;
        }

        return true;
    }

    /**
     * �f�[�^��o�^����B
     *
     * @param con ConnectionWrapper
     * @return true - �����Afalse - ���s
     * @throws java.sql.SQLException SQLException
     */
    private boolean registDataRankItemDetail(ConnectionWrapper con, Integer itemGroupId) throws SQLException {
        for (ProductClass pc : selectProducts) {
            for (Product p : pc) {
                if (con.executeUpdate(this.getInsertRankItemDetailSQL(p, itemGroupId)) != 1) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * �P�����̃f�[�^��o�^����r�p�k�����擾����B
     *
     * @param p �o�^���鏤�i�E�Z�p
     * @return �P�����̃f�[�^��o�^����r�p�k��
     */
    private String getInsertRankItemDetailSQL(Product p, Integer itemGroupId) {
        return "insert into mst_rank_item_detail \n"
                + "(item_group_id, item_id, \n"
                + "insert_date, update_date, delete_date) \n"
                + "values( " + itemGroupId + ",\n"
                + SQLUtil.convertForSQL(p.getProductID()) + ",\n"
                + "current_timestamp,\n"
                + "current_timestamp,\n"
                + "null) \n";
    }

    /**
     * ���̃f�[�^���폜����B
     *
     * @param con ConnectionWrapper
     * @return true - �����Afalse - ���s
     * @throws java.sql.SQLException SQLException
     */
    private boolean deleteDataRankItemDetail(ConnectionWrapper con, Integer itemGroupId) throws SQLException {
        return (0 <= con.executeUpdate(this.getDeleteRankItemDetailSQL(itemGroupId)));
    }

    /**
     * ���̃f�[�^���폜����r�p�k�����擾����B
     *
     * @return ���̃f�[�^���폜����r�p�k��
     */
    private String getDeleteRankItemDetailSQL(Integer itemGroupId) {
        return "delete from mst_rank_item_detail\n"
                + "where item_group_id = " + itemGroupId;

    }

}

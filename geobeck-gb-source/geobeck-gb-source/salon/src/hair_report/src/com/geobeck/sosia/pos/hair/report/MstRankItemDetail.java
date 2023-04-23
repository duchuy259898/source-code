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
     * 処理区分（1：技術、2：商品）
     */
    private Integer productDivision = null;

    /**
     * 参照商品・技術
     */
    private ProductClasses referenceProducts = new ProductClasses();
    /**
     * 選択商品・技術
     */
    private ProductClasses selectProducts = new ProductClasses();

    /**
     * コンストラクタ
     */
    public MstRankItemDetail() {
    }

    /**
     * 処理区分（1：技術、2：商品）を取得する。
     *
     * @return 処理区分（1：技術、2：商品）
     */
    public Integer getProductDivision() {
        return productDivision;
    }

    /**
     * 処理区分（1：技術、2：商品）を設定する。
     *
     * @param productDivision 処理区分（1：技術、2：商品）
     */
    public void setProductDivision(Integer productDivision) {
        this.productDivision = productDivision;
        referenceProducts.setProductDivision(productDivision);
        selectProducts.setProductDivision(productDivision);
    }

    /**
     * 処理区分名を取得する。
     *
     * @return 処理区分名
     */
    public String getProductDivisionName() {
        switch (productDivision) {
            case 1:
                return "店舗使用技術";
            case 2:
                return "店舗使用商品";
            default:
                return "";
        }
    }

    /**
     * 参照商品・技術を取得する。
     *
     * @return 参照商品・技術
     */
    public ProductClasses getReferenceProducts() {
        return referenceProducts;
    }

    /**
     * 参照商品・技術を設定する。
     *
     * @param referenceProducts 参照商品・技術
     */
    public void setReferenceProducts(ProductClasses referenceProducts) {
        this.referenceProducts = referenceProducts;
    }

    /**
     * 選択商品・技術を取得する。
     *
     * @return 選択商品・技術
     */
    public ProductClasses getSelectProducts() {
        return selectProducts;
    }

    /**
     * 選択商品・技術を設定する。
     *
     * @param selectProducts 選択商品・技術
     */
    public void setSelectProducts(ProductClasses selectProducts) {
        this.selectProducts = selectProducts;
    }

    /**
     * データを読み込む。
     *
     * @param con ConnectionWrapper
     * @return true - 成功、false - 失敗
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
     * 取扱商品・技術を読み込む。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
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
     * 取扱商品・技術を読み込むＳＱＬ文を取得する。
     *
     * @return 取扱商品・技術を読み込むＳＱＬ文
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
     * 商品・技術を選択（解除）する。
     *
     * @param isSelect true：選択、false：解除
     * @param productClassIndex 分類のインデックス
     * @param productIndex 商品・技術のインデックス
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
     * 商品・技術を全て選択（解除）する。
     *
     * @param isSelect true：選択、false：解除
     * @param productClassIndex 分類のインデックス
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
     * 登録処理を行う。
     *
     * @param con ConnectionWrapper
     * @return true - 成功、false - 失敗
     * @throws java.sql.SQLException SQLException
     */
    public boolean registRankItemDetail(ConnectionWrapper con, Integer itemGroupId) throws SQLException {
        //元データを削除
        if (!this.deleteDataRankItemDetail(con, itemGroupId)) {
            return false;
        }
        //データを登録
        if (!this.registDataRankItemDetail(con, itemGroupId)) {
            return false;
        }

        return true;
    }

    /**
     * データを登録する。
     *
     * @param con ConnectionWrapper
     * @return true - 成功、false - 失敗
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
     * １件分のデータを登録するＳＱＬ文を取得する。
     *
     * @param p 登録する商品・技術
     * @return １件分のデータを登録するＳＱＬ文
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
     * 元のデータを削除する。
     *
     * @param con ConnectionWrapper
     * @return true - 成功、false - 失敗
     * @throws java.sql.SQLException SQLException
     */
    private boolean deleteDataRankItemDetail(ConnectionWrapper con, Integer itemGroupId) throws SQLException {
        return (0 <= con.executeUpdate(this.getDeleteRankItemDetailSQL(itemGroupId)));
    }

    /**
     * 元のデータを削除するＳＱＬ文を取得する。
     *
     * @return 元のデータを削除するＳＱＬ文
     */
    private String getDeleteRankItemDetailSQL(Integer itemGroupId) {
        return "delete from mst_rank_item_detail\n"
                + "where item_group_id = " + itemGroupId;

    }

}

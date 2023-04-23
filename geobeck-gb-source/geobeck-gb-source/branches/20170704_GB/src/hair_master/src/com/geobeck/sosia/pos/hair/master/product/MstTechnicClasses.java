/*
 * MstTechnicClasses.java
 *
 * Created on 2006/05/24, 15:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.master.product;

import com.geobeck.sosia.pos.master.MstData;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;

/**
 * �Z�p���ރ}�X�^�f�[�^��ArrayList
 *
 * @author katagiri
 */
public class MstTechnicClasses extends ArrayList<MstTechnicClass> {
    
    /**
     * �R���X�g���N�^
     */
    public MstTechnicClasses() {
    }

    /**
     * �Z�p���ރ}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
     */
    public void getAll(ConnectionWrapper con) throws SQLException {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(
                MstTechnicClasses.getSelectSQL());

        while (rs.next()) {
            MstTechnicClass mtc = new MstTechnicClass();
            mtc.setData(rs);
            this.add(mtc);
        }

        rs.close();
    }
     /**
     * LUC start add ���j���[�I�� LOAD TECHNIC_CLASS SQL 20121030
     */
    public void load(ConnectionWrapper con) throws SQLException {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(
                MstTechnicClasses.getSelectSQL());

        while (rs.next()) {
            MstTechnicClass mtc = new MstTechnicClass();
            mtc.setData(rs);
            this.add(mtc);
        }

        rs.close();
    }
    /**
     * LUC start add ���j���[�I�� LOAD TECHNIC_CLASS SQL 20121030
     */
    public void loadAll(ConnectionWrapper con, Integer productDivision) throws SQLException {
        this.clear();

        ResultSetWrapper rs = con.executeQuery(
                MstTechnicClasses.getAllSelectSQL(productDivision));

        while (rs.next()) {
            MstTechnicClass mtc = new MstTechnicClass();
            mtc.setData(rs);
            this.add(mtc);
        }

        rs.close();
    }
    //Thuyet End add ���j���[�I�� LOAD SQL 20130313
    
    /**
     * �Z�p���ރ}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
     *
     * @return �Z�p���ރ}�X�^�f�[�^��S�Ď擾����r�p�k��
     */
    public static String getAllSelectSQL(Integer productDivision) {
        StringBuilder sql = new StringBuilder(1000);
        switch(productDivision)
        {
            case 1:
                sql.append(" select");
                sql.append("      a.*");
                sql.append("     ,b.technic_integration_name");
                sql.append("     ,b.display_seq");
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
                sql.append("      ,c.shop_class_name");
                // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
                sql.append(" from");
                sql.append("     mst_technic_class a");
                sql.append("         left join (select * from mst_technic_integration where delete_date is null) b");
                sql.append("         using (technic_integration_id)");
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
                sql.append(" left join mst_shop_category c ");
                sql.append(" on a.shop_category_id =  c.shop_category_id ");
                // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
                sql.append(" where");
                sql.append("     a.delete_date is null");
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
                sql.append(" and c.delete_date is null ");
                // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
                sql.append(" order by");
                sql.append("      a.display_seq");
                // IVS_LeTheHieu Start delete 20140703 GB_MASHU_�Z�p���ޓo�^
                //sql.append("     ,a.technic_class_name");
                // IVS_LeTheHieu End delete 20140703 GB_MASHU_�Z�p���ޓo�^

                break;
            //���i
            case 2:
                sql.append(" select");
                sql.append( "     a.item_class_id as technic_class_id,");
                sql.append("      a.item_class_name as technic_class_name,");
                sql.append("      a.item_class_contracted_name as technic_class_contracted_name,");
                sql.append("      a.item_integration_id as technic_integration_id,");
                sql.append("      a.display_seq,");
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
                sql.append("      a.shop_category_id,");
                sql.append("      c.shop_class_name,");
                // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
                sql.append("      0 as prepaid,");
                sql.append("      b.item_integration_name as technic_integration_name,");
                sql.append("     b.display_seq");
                sql.append(" from");
                sql.append("     mst_item_class a");
                sql.append("         left join (select * from mst_item_integration where delete_date is null) b");
                sql.append("         using (item_integration_id)");
                 // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
                 sql.append(" left join mst_shop_category c ");
                sql.append(" on a.shop_category_id =  c.shop_category_id ");
                // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
                sql.append(" where");
                sql.append("     a.delete_date is null");
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
                sql.append(" and c.delete_date is null ");
                // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
                sql.append(" order by");
                sql.append("      a.display_seq");
                // IVS_LeTheHieu Start delete 20140703 GB_MASHU_�Z�p���ޓo�^
                //sql.append("     ,a.technic_class_name");
                // IVS_LeTheHieu End delete 20140703 GB_MASHU_�Z�p���ޓo�^
                break;           

        }
        return sql.toString();
    }
    /**
     * �Z�p���ރ}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
     *
     * @return �Z�p���ރ}�X�^�f�[�^��S�Ď擾����r�p�k��
     */
    public static String getSelectSQL() {
        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("      a.*");
        sql.append("     ,b.technic_integration_name");
        sql.append("     ,b.display_seq");
        // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
        sql.append("     ,c.shop_class_name ");
        sql.append("     ,c.shop_category_id ");
        // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
        sql.append(" from");
        sql.append("     mst_technic_class a");
        sql.append("         left join (select * from mst_technic_integration where delete_date is null) b");
        sql.append("         using (technic_integration_id)");
        // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
        sql.append(" left join mst_shop_category c ");
        sql.append(" on a.shop_category_id =  c.shop_category_id ");
        // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
        sql.append(" where");
        sql.append("     a.delete_date is null");
        // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
        sql.append(" and c.delete_date is null ");
        // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
        sql.append(" order by");
        sql.append("      a.display_seq");
        // IVS_LeTheHieu Start delete 20140703 GB_MASHU_�Z�p���ޓo�^
        //sql.append("     ,a.technic_class_name");
        // IVS_LeTheHieu End delete 20140703 GB_MASHU_�Z�p���ޓo�^

       return sql.toString();
    }
}

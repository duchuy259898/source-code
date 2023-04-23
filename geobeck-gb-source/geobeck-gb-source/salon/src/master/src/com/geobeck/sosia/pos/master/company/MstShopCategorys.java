/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master.company;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * �Ƒԃ}�X�^�ꗗ
 * @author ttmloan
 */
public class MstShopCategorys extends ArrayList<MstShopCategory> {

    /**
     * �Ƒԃ}�X�^����A�ݒ肳��Ă���X��ID�̃f�[�^��ǂݍ��ށB
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public void loadByShop(ConnectionWrapper con,Integer shopId) throws SQLException {
        if (con != null) {
            ResultSetWrapper rs = con.executeQuery(this.getSelectByShopSQL(shopId));
            while (rs.next()) {
                MstShopCategory element = new MstShopCategory();
                element.setData(rs);
                this.add(element);
            }
        }
    }
    
    
    
    /**
     * Select�����擾����B
     *
     * @return Select��
     */
    private String getSelectByShopSQL(Integer shopId) {
        //IVS_vtnhan start edit 20140814 MASHU_�S���ė�����
        return " select "
               + " mc.shop_category_id, mc.shop_class_name, mc.display_seq "
               + " from mst_shop_category mc "							
               + " inner join mst_shop_relation mr "					
               + "      on  mc.shop_category_id = mr.shop_category_id "						
               + " where "								
               + " mr.shop_id = " + shopId	
               + " and mr.delete_date is null "
               + " and mc.delete_date is null "
               + " order by mc.display_seq";
        //IVS_vtnhan start edit 20140814 MASHU_�S���ė�����
    }
    
   
    
    //IVS VUINV start add 20140708 MASHU_�{�p��o�^
    
    public void loadBusinessCategory(ConnectionWrapper con, Integer shopId, Integer bedId) throws SQLException {
        if (con != null) {
            
            this.clear();
            
            ResultSetWrapper rs = con.executeQuery(this.getSQL(shopId, bedId));
            while (rs.next()) {
                MstShopCategory element = new MstShopCategory();
                element.setShopCategoryId(rs.getInt("shop_category_id"));
                element.setShopClassName(rs.getString("shop_class_name"));
                element.setIsCheck(rs.getBoolean("is_check"));
                this.add(element);
            }
        }
    }
    
    /**
     * get list mstshopcategory
     * @param shopId
     * @param bedId
     * @return 
     */
    private String getSQL(Integer shopId, Integer bedId) {
        return "SELECT shop_class_name,  msc.shop_category_id, "
                        + "EXISTS (SELECT shop_category_id FROM mst_bed_relation "
                        + "WHERE bed_id = " + bedId
                        + " AND shop_id = "+ shopId
                        + " AND shop_category_id = msc.shop_category_id) as is_check "
                        + "FROM mst_shop_category msc "
                        + "INNER JOIN mst_shop_relation msr ON msc.shop_category_id = msr.shop_category_id "
                        + "WHERE msr.shop_id = " + shopId
                        + " AND msc.delete_date IS NULL "
                        + " ORDER BY msc.display_seq ";
    }
    
    //IVS VUINV start end 20140708 MASHU_�{�p��o�^
 
}

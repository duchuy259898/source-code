/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master.company;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 * �X�܋ƑԊ֘A�}�X�^
 *
 * @author IVS_TTMLoan
 * @since 2014/07/07
 */
public class MstShopRelation {

    /**
     * �X��ID
     */
    private Integer shopId;
    /**
     * �Ƒ�ID
     */
    private Integer shopCategoryId;
    /**
     * �ƑԖ�
     */
    private String shopClassName;

    /**
     * �\����
     */
    private Integer displaySeq;
    
    /**
     * �Ƒ�ID���擾����
     *
     * @return �Ƒ�ID
     */
    public Integer getShopCategoryId() {
        return shopCategoryId;
    }

    /**
     * �Ƒ�ID��ݒ肷��
     *
     * @param shopCategoryId �Ƒ�ID
     */
    public void setShopCategoryId(Integer shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    /**
     * �X��ID���擾����
     *
     * @return �X��ID
     */
    public Integer getShopId() {
        return shopId;
    }

    /**
     * �X��ID��ݒ肷��
     *
     * @param shopId �X��ID
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    /**
     * �ƑԖ����擾����
     *
     * @return �ƑԖ�
     */
    public String getShopClassName() {
        return shopClassName;
    }

    /**
     * �ƑԖ���ݒ肷��
     *
     * @param shopClassName �ƑԖ�
     */
    public void setShopClassName(String shopClassName) {
        this.shopClassName = shopClassName;
    }

    /**
     * �\�������擾����
     * @return �\����
     */
    public Integer getDisplaySeq() {
        return displaySeq;
    }

    /**
     * �ƑԖ���ݒ肷��
     * @param displaySeq �\����
     */
    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }

    
    
    /**
     * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setShopCategoryId(rs.getInt("shop_category_id"));
        this.setShopClassName(rs.getString("shop_class_name"));
        this.setShopId(rs.getInt("shop_id"));
    }
    
    /**
     * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     * @author IVS_TTMLoan
     * @since 2014/07/25
     */
    public void setDataWithSeq(ResultSetWrapper rs) throws SQLException {
        this.setShopCategoryId(rs.getInt("shop_category_id"));
        this.setShopClassName(rs.getString("shop_class_name"));
        this.setShopId(rs.getInt("shop_id"));
        this.setDisplaySeq(rs.getInt("display_seq"));
    }
    
    /**
     * �X�܋ƑԊ֘A�}�X�^�Ƀf�[�^��o�^����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true ����
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        if (con != null) {
            return (con.executeUpdate(this.getInsertSQL()) == 1);
        }
        return false;
    }

    /**
     * �X�܋ƑԊ֘A�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (con != null) {

            ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

            if (rs.next()) {
                return true;
            }
        }
        return false;

    }

    /**
     * Select�����擾����B
     *
     * @return Select��
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    private String getSelectSQL() {
        return "SELECT * "
                + " FROM mst_shop_relation"
                + " WHERE shop_id = " + SQLUtil.convertForSQL(this.shopId)
                + " AND shop_category_id = " + SQLUtil.convertForSQL(this.shopCategoryId)
                + " AND delete_date IS NULL";
    }

    /**
     * Insert�����擾����B
     *
     * @return Insert��
     * @author IVS_TTMLoan
     * @since 2014/07/07
     */
    private String getInsertSQL() {
        return "insert into mst_shop_relation"
                + " (shop_id, shop_category_id, insert_date, update_date) values("
                + SQLUtil.convertForSQL(this.getShopId()) + ","
                + SQLUtil.convertForSQL(this.getShopCategoryId()) + ","
                + "current_timestamp, current_timestamp)";
    }
	
	//GB_Mashu nahoang Start add 2014/10/06_�ڕW�ݒ�
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("");

        return sb.toString() + this.getShopClassName();
    }
    // GB_Mashu nahoang end add 2014/10/06_�ڕW�ݒ�
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master.company;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;

/**
 * �Ƒԃ}�X�^
 * @author ttmloan
 */
public class MstShopCategory {
    /** �Ƒ�ID */
    private Integer shopCategoryId;
    /** �ƑԖ� */
    private String shopClassName;

    //IVS_vtnhan start add 20140805 MASHU_�S���ė�����
    private Integer displaySeq;
    //IVS_vtnhan end add 20140805 MASHU_�S���ė�����
    public Integer getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(Integer shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public String getShopClassName() {
        return shopClassName;
    }

    public void setShopClassName(String shopClassName) {
        this.shopClassName = shopClassName;
    }
    
    // IVS_LeTheHieu Start add 20140709 MASHU_�R�[�X���ޓo�^
    private Integer courseintegrationid;
    
    private String  courseintegrationname;

    public Integer getCourseintegrationid() {
        return courseintegrationid;
    }

    public void setCourseintegrationid(Integer courseintegrationid) {
        this.courseintegrationid = courseintegrationid;
    }

    public String getCourseintegrationname() {
        return courseintegrationname;
    }

    public void setCourseintegrationname(String courseintegrationname) {
        this.courseintegrationname = courseintegrationname;
    }
    
    // IVS_LeTheHieu Start end 20140709 MASHU_�R�[�X���ޓo�^
    
    //IVS VUINV start add 20140708 MASHU_�{�p��o�^
    
    private boolean isCheck;

    public boolean isIsCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
    
    //IVS VUINV start add 20140708 MASHU_�{�p��o�^
    //IVS_vtnhan start add 20140805 MASHU_�S���ė�����
     public Integer getDisplaySeq() {
        return displaySeq;
    }

    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }
    //IVS_vtnhan end add 20140805 MASHU_�S���ė�����
    
    /**
	 * �Ƒԃ}�X�^����A�ݒ肳��Ă���X��ID�̃f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean loadAll(ConnectionWrapper con) throws SQLException{
		if(con == null)	return	false;
		ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
		if(rs.next()){
                    this.setData(rs);
                    return true;
		}
		return	false;
	}
        
       /**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select * from mst_shop_category "
                        + "where delete_date is null "
                        + "order by  shop_category_id";
	}
        
	/**
	 * ResultSetWrapper�̃f�[�^��ǂݍ��ށB
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
            this.setShopCategoryId(rs.getInt("shop_category_id"));
            this.setShopClassName(rs.getString("shop_class_name"));
            //IVS_vtnhan start add 20140814 MASHU_�S���ė�����
            this.setDisplaySeq(rs.getInt("display_seq"));
            //IVS_vtnhan end add 20140814 MASHU_�S���ė�����
	}
        
        /**
         * �ƑԖ���\������
         * @return shopClassName
         */
        @Override
        public String toString() {
            return shopClassName;
        }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master.company;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;

/**
 * ‹Æ‘Ôƒ}ƒXƒ^
 * @author ttmloan
 */
public class MstShopCategory {
    /** ‹Æ‘ÔID */
    private Integer shopCategoryId;
    /** ‹Æ‘Ô–¼ */
    private String shopClassName;

    //IVS_vtnhan start add 20140805 MASHU_’S“–Ä—ˆ•ªÍ
    private Integer displaySeq;
    //IVS_vtnhan end add 20140805 MASHU_’S“–Ä—ˆ•ªÍ
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
    
    // IVS_LeTheHieu Start add 20140709 MASHU_ƒR[ƒX•ª—Ş“o˜^
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
    
    // IVS_LeTheHieu Start end 20140709 MASHU_ƒR[ƒX•ª—Ş“o˜^
    
    //IVS VUINV start add 20140708 MASHU_{p‘ä“o˜^
    
    private boolean isCheck;

    public boolean isIsCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
    
    //IVS VUINV start add 20140708 MASHU_{p‘ä“o˜^
    //IVS_vtnhan start add 20140805 MASHU_’S“–Ä—ˆ•ªÍ
     public Integer getDisplaySeq() {
        return displaySeq;
    }

    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }
    //IVS_vtnhan end add 20140805 MASHU_’S“–Ä—ˆ•ªÍ
    
    /**
	 * ‹Æ‘Ôƒ}ƒXƒ^‚©‚çAİ’è‚³‚ê‚Ä‚¢‚é“X•ÜID‚Ìƒf[ƒ^‚ğ“Ç‚İ‚ŞB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ¬Œ÷
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
	 * Select•¶‚ğæ“¾‚·‚éB
	 * @return Select•¶
	 */
	private String getSelectSQL()
	{
		return	"select * from mst_shop_category "
                        + "where delete_date is null "
                        + "order by  shop_category_id";
	}
        
	/**
	 * ResultSetWrapper‚Ìƒf[ƒ^‚ğ“Ç‚İ‚ŞB
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
            this.setShopCategoryId(rs.getInt("shop_category_id"));
            this.setShopClassName(rs.getString("shop_class_name"));
            //IVS_vtnhan start add 20140814 MASHU_’S“–Ä—ˆ•ªÍ
            this.setDisplaySeq(rs.getInt("display_seq"));
            //IVS_vtnhan end add 20140814 MASHU_’S“–Ä—ˆ•ªÍ
	}
        
        /**
         * ‹Æ‘Ô–¼‚ğ•\¦‚·‚é
         * @return shopClassName
         */
        @Override
        public String toString() {
            return shopClassName;
        }
}

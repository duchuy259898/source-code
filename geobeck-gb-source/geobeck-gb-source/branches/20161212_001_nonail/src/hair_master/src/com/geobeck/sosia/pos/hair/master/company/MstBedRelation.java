/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.company;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author VUINV
 */
public class MstBedRelation {
    
        /**
	 * “X•ÜID
	 */
	protected Integer shopID = null;
        
        /**
	 * Ž{p‘äID
	 */
	protected Integer bedID	= null;
        
        /**
	 * ‹Æ‘ÔID
	 */
	protected Integer shopCategoryID = null;
        
        /**
	 * “o˜^“ú
	 */
	protected Date insertDate = null;
        
        /**
	 * XV“ú
	 */
	protected Date updateDate = null;
        
        /**
	 * íœ“ú
	 */
	protected Date deleteDate = null;

        public Integer getShopID() {
            return shopID;
        }

        public void setShopID(Integer shopID) {
            this.shopID = shopID;
        }

        public Integer getBedID() {
            return bedID;
        }

        public void setBedID(Integer bedID) {
            this.bedID = bedID;
        }

        public Integer getShopCategoryID() {
            return shopCategoryID;
        }

        public void setShopCategoryID(Integer shopCategoryID) {
            this.shopCategoryID = shopCategoryID;
        }

        public Date getInsertDate() {
            return insertDate;
        }

        public void setInsertDate(Date insertDate) {
            this.insertDate = insertDate;
        }

        public Date getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(Date updateDate) {
            this.updateDate = updateDate;
        }

        public Date getDeleteDate() {
            return deleteDate;
        }

        public void setDeleteDate(Date deleteDate) {
            this.deleteDate = deleteDate;
        }

        public boolean delete(ConnectionWrapper con) throws SQLException {
            if(con == null)	return	false;
            return con.executeUpdate(getDeleletSQL()) > 1;
        }

        private String getDeleletSQL()
        {
            return "DELETE FROM mst_bed_relation "
                    + "WHERE shop_id = " + this.getShopID() 
                    + " AND bed_id = " + this.getBedID();
        }

        public boolean insert(ConnectionWrapper con) throws SQLException {
            if(con == null)	return	false;
            return con.executeUpdate(getInsertSQL()) > 1;
        }

        private String getInsertSQL()
        {
            return "INSERT INTO mst_bed_relation("
                    + "shop_id, "
                    + "bed_id, "
                    + "shop_category_id, "
                    + "insert_date, "
                    + "update_date) "
                    + "VALUES ("
                    + SQLUtil.convertForSQL(this.getShopID()) + ", "
                    + SQLUtil.convertForSQL(this.getBedID()) + ", "
                    + SQLUtil.convertForSQL(this.getShopCategoryID()) + ", "
                    + "current_timestamp, "
                    + "current_timestamp)";
        }
    
}

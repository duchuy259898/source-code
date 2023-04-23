/*
 * MstStaff.java
 *
 * Created on 2006/04/26, 9:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import java.sql.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �X�^�b�t���o�^
 * @author vtnhan
 */
public class MstStaffRelation
{ 
	/**
	 * �X�^�b�tID
	 */
	private	Integer			staffID			=	null;
	/**
	 * �Ƒ�ID
	 */
	private	Integer			shopCategoryId			=	null;
        
        
        private String                  shopClassName                   = "";  
        
        private boolean                  isCheck                         = false;
        
        

        public boolean getIsCheck() {
            return isCheck;
        }

        public void setIsCheck(boolean isCheck) {
            this.isCheck = isCheck;
        }
        
	/**
	 * �o�^��
	 */
	private Timestamp insertDate = null;
	/**
	 * �X�V��
	 */
	private Timestamp updateDate = null;
	/**
	 * �폜��
	 */
	private Timestamp deleteDate = null;
	
        /**
         * 
         * @return 
         */
        public Integer getStaffID() {
            return staffID;
        }

        /**
         * 
         * @param staffID 
         */
        public void setStaffID(Integer staffID) {
            this.staffID = staffID;
        }

        /**
         * 
         * @return 
         */
        public Integer getShopCategoryId() {
            return shopCategoryId;
        }

        public String getShopClassName() {
            return shopClassName;
        }

        public void setShopClassName(String shopClassName) {
            this.shopClassName = shopClassName;
        }

        /**
         * 
         * @param shopCategoryId 
         */
        public void setShopCategoryId(Integer shopCategoryId) {
            this.shopCategoryId = shopCategoryId;
        }

        /**
         * 
         * @return 
         */
        public Timestamp getInsertDate() {
            return insertDate;
        }

        /**
         * 
         * @param insertDate 
         */
        public void setInsertDate(Timestamp insertDate) {
            this.insertDate = insertDate;
        }

        /**
         * 
         * @return 
         */
        public Timestamp getUpdateDate() {
            return updateDate;
        }

        /**
         * 
         * @param updateDate 
         */
        public void setUpdateDate(Timestamp updateDate) {
            this.updateDate = updateDate;
        }

        /**
         * 
         * @return 
         */
        public Timestamp getDeleteDate() {
            return deleteDate;
        }

        /**
         * 
         * @param deleteDate 
         */
        public void setDeleteDate(Timestamp deleteDate) {
            this.deleteDate = deleteDate;
        }

        
        /**
         * delete mst_staff_relation with staff_id (�X�^�b�t���o�^)
         * @return 
         */
	private String	getDeleteAllSQL()
	{
            
            StringBuilder strSql = new StringBuilder();
            strSql.append("delete  from mst_staff_relation where staff_id = " + SQLUtil.convertForSQL(this.getStaffID()) + "\n");
      
            return strSql.toString();
	}
        
        
        /**
         * regist data into table mst_staff_relation
         * @param con
         * @return
         * @throws SQLException 
         */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		
		String	sql		=	"";
                
                if(con== null)
                    return false;
		if(this.getIsCheck()){
                    sql	=	this.getInsertSQL(); 
                }
                if(con.executeUpdate(sql) == 1)
                {
                        return	true;
                }

		return	false;
		
	}
        
        
       /**
        * check exist mst_staff_relation
        * @param con
        * @return
        * @throws SQLException 
        */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
            boolean result = false;
            ResultSetWrapper	rs	=	con.executeQuery(this.getMstStaffRelation());
            if(rs.next()){
                result = true;
            }	
            return	result;
		
	}
        
        /**
         * sql check exist
         * @param staffId
         * @param shopCateGoryId
         * @return 
         */
        private String getMstStaffRelation(){
            
            StringBuilder sql = new StringBuilder();
            sql.append("select * from mst_staff_relation where staff_id = " + SQLUtil.convertForSQL(this.getStaffID()));
            sql.append(" and shop_category_id = " + SQLUtil.convertForSQL(this.getShopCategoryId()));
            return sql.toString();
        }
        
        
        /**
         * sql insert mst_staff_relation (�X�^�b�t���o�^)
         * @return 
         */
	private String	getInsertSQL()
	{  
            StringBuilder strSql = new StringBuilder();
            strSql.append("insert into mst_staff_relation\n");
            strSql.append("(staff_id, shop_category_id, insert_date, update_date, delete_date\n");          
            strSql.append(" ) values (");
            strSql.append("      " + SQLUtil.convertForSQL(this.getStaffID()));
            strSql.append("     ," + SQLUtil.convertForSQL(getShopCategoryId()));
            strSql.append("    ,current_timestamp " );
            strSql.append("    ,current_timestamp " );
            strSql.append("    ,null");
            strSql.append(" )");
     
            return strSql.toString();	
	}

        /**
	 * MstStaffRelation �X�^�b�t���o�^
	 * @param ms MstStaffRelation
	 */
	public void setData(ResultSetWrapper rs )throws SQLException
	{
		this.setShopCategoryId(rs.getInt("shop_category_id"));
                this.setShopClassName(rs.getString("shop_class_name"));
                if(rs.getString("checkstatus") != null){
                    this.setIsCheck(true);
                }else{
                     this.setIsCheck(false);
                }
	
	}
        
         /**
         * delete mst_staff_relation
         * @param con
         * @return
         * @throws SQLException 
         */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
                if(con == null){
                    return false;
                }else{
                    
                    Integer result = con.executeUpdate(this.getDeleteAllSQL());
                    if(result >= 0)
                    {
                            return	true;
                    }
                }

                return	false;
	}
        
        /**
         * clear mst_staff_relation
         */
        public void clear()
	{
		this.setStaffID(null);
                this.setShopCategoryId(null);
                this.setShopClassName(null);
		this.setInsertDate(null);
                this.setUpdateDate(null);
                this.setDeleteDate(null);
		
	}
        
        
}

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
import java.util.*;
import com.geobeck.sql.*;

/**
 * ÉXÉ^ÉbÉtèÓïÒìoò^
 * @author vtnhan
 */
public class MstStaffRelations extends ArrayList<MstStaffRelation>
{ 
    
        private MstStaff staff = null;
        private Integer shopId = null;

        public MstStaff getStaff() {
            return staff;
        }

        public void setStaff(MstStaff staff) {
            this.staff = staff;
        }
        
        public Integer getShopId() {
            return shopId;
        }

        public void setShopId(Integer shopId) {
            this.shopId = shopId;
        }
       
        /**
         * 
         */
        public MstStaffRelations(){
            
        }
        
   
        /**
        * function load data from mst_staff_relation
        */
       public void load(ConnectionWrapper con) throws SQLException {
           this.clear();
           ResultSetWrapper rs = con.executeQuery(
                   MstStaffRelations.getSelectSQL(this.getStaff().getStaffID(),this.shopId));
           while (rs.next()) {
               MstStaffRelation msr = new MstStaffRelation();
               msr.setData(rs);
               this.add(msr);
           }

           rs.close();
       }

        
        /**
         * sql get data show for ÉXÉ^ÉbÉtèÓïÒìoò^ screen
         * @return 
         */
        private static String getSelectSQL(Integer staffId, Integer shopId)
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select cate.shop_category_id, cate.shop_class_name,staff.shop_category_id as checkstatus ");
            sql.append(" from mst_shop_category cate inner join mst_shop_relation relation ");
            sql.append(" on cate.shop_category_id = relation.shop_category_id ");
            sql.append(" left join mst_staff_relation staff  ");
            sql.append(" on relation.shop_category_id= staff.shop_category_id ");
            sql.append(" and staff.staff_id = " + staffId);
            sql.append(" where ");
            
            sql.append("    cate.delete_date is null ");
            sql.append(" and relation.delete_date is null ");
            sql.append(" and staff.delete_date is null ");
            sql.append(" and relation.shop_id =  " + shopId);
            sql.append(" order by display_seq ");
            
     
            return sql.toString();
	}
        
}

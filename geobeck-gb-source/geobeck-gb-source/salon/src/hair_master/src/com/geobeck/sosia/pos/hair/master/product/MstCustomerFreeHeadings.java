/*
 * MstCustomerFreeHeadings.java
 *
 * Created on 2007/08/20, 19:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import com.geobeck.sql.ResultSetWrapper;
import java.util.*;
import com.geobeck.util.*;
import java.sql.SQLException;

/**
 *
 * @author kanemoto
 */
public class MstCustomerFreeHeadings extends ArrayList<MstCustomerFreeHeading> {

	/** Creates a new instance of MstCustomerFreeHeadings */

         protected String            freeHeadingText    =  "";
         protected Integer  freeHeadingClassID = null;
         protected Integer	      freeHeadingID    = null;
	public MstCustomerFreeHeadings() {
	}

        public String getFreeHeadingText() {
            return freeHeadingText;
        }

        public void setFreeHeadingText(String freeHeadingText) {
            this.freeHeadingText = freeHeadingText;
        }

        public Integer getFreeHeadingClassID() {
            return freeHeadingClassID;
        }

        public void setFreeHeadingClassID(Integer freeHeadingClassID) {
            this.freeHeadingClassID = freeHeadingClassID;
        }

        public Integer getFreeHeadingID() {
            return freeHeadingID;
        }

        public void setFreeHeadingID(Integer freeHeadingID) {
            this.freeHeadingID = freeHeadingID;
        }


	/**
         *
         *
	 * Selectï∂ÇéÊìæÇ∑ÇÈÅB
	 * @return Selectï∂
	 */



	private String getSelectSQL( Integer customerID )
	{
		return
			"select\n" +
			"*\n" +
			"from\n" +
			"mst_free_heading_class as mfhc\n" +
			"left join mst_customer_free_heading as mcfh on\n" +
			"mcfh.delete_date is null\n" +
			"and mcfh.customer_id = " + SQLUtil.convertForSQL( customerID ) + "\n" +
			"and mcfh.free_heading_class_id = mfhc.free_heading_class_id\n" +
			"left join mst_customer as mc on\n" +
			"mc.delete_date is null\n" +
			"and mc.customer_id = mcfh.customer_id\n" +
			"left join mst_free_heading as mfh on\n" +
			"mfh.delete_date is null\n" +
			"and mfh.free_heading_class_id = mfhc.free_heading_class_id\n" +
			"and mfh.free_heading_id = mcfh.customer_id\n" +
			"where\n" +
			"mfhc.delete_date is null\n" +
			"and mfhc.use_type = 't'\n" +
			"order by\n" +
			"mfhc.free_heading_class_id\n" +
			";\n";
	}

        public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setFreeHeadingClassID(rs.getInt("free_heading_class_id"));
		this.setFreeHeadingID(rs.getInt("free_heading_id"));
                this.setFreeHeadingText(rs.getString("free_heading_text"));

	}

}

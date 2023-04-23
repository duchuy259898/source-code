/*
 * MstCustomerFreeHeadings.java
 *
 * Created on 2007/08/20, 19:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import java.util.*;
import com.geobeck.util.*;

/**
 *
 * @author kanemoto
 */
public class MstCustomerFreeHeadings extends ArrayList<MstCustomerFreeHeading> {
	
	/** Creates a new instance of MstCustomerFreeHeadings */
	public MstCustomerFreeHeadings() {
	}
	
	/**
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
    
}

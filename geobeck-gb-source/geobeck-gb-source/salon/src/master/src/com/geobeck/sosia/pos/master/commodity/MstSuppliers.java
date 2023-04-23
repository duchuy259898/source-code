/*
 * MstSuppliers.java
 *
 * Created on 2008/09/11, 16:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.commodity;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author s_matsumura
 */
public class MstSuppliers extends ArrayList<MstSupplier>{
    
    private String		supplierList		=	"";
    
    /** Creates a new instance of MstSuppliers */
    public MstSuppliers() {
    }
    
    public String getSupplierList()
	{
		return supplierList;
	}
    
    public void setSupplierList(String supplierList)
	{
		this.supplierList = supplierList;
	}
    
    
    /**
	 * édì¸êÊÇì«Ç›çûÇﬁ
	 * @param con ConnectionWrapper
	 * @return true - ê¨å˜
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con, boolean isAddBlank) throws SQLException
	{
		this.clear();
		
		if(isAddBlank)
		{
			this.add(new MstSupplier());
		}
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSupplierSelectSQL());

		while(rs.next())
		{
			MstSupplier		ms		=	new MstSupplier();
			ms.setData(rs);

			this.add(ms);
		}
		
		return	true;
	}
	
	public String getSupplierSelectSQL()
	{
		return	"select *\n" +
			"from mst_supplier\n" +
			"where delete_date is null\n" +
			"order by supplier_no\n";
	}
	
	public int getIndexByID(Integer supplierID)
	{	
		for(int i = 0; i < this.size(); i ++)
		{
			MstSupplier ms	=	this.get(i);
			if(ms.getSupplierID() != null && ms.getSupplierID().equals( supplierID ))
			{
				return	i;
			}
		}
		
		return	-1;
	}
    
}

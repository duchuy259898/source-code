/*
 * SearchSupplier.java
 *
 * Created on 2008/12/11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.search.supplier;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import com.geobeck.sql.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.master.commodity.*;

/**
 *
 * @author katagiri
 */
public class SearchSupplier extends ArrayList<MstSupplier>
{
	private	Integer	supplierNoFrom	    =	null;
	private	Integer supplierNoTo	    =	null;
	private String	supplierName	    =	"";
	private Integer	purchaseDivision    =	0;
	private	String	prefecture	    =	"";
	private String	phoneNumber	    =	"";
	private	Integer	itemID		    =	null;
	
	/** Creates a new instance of SearchSupplier */
	public SearchSupplier() {
	}

	public Integer getSupplierNoFrom() {
	    return supplierNoFrom;
	}

	public void setSupplierNoFrom(Integer supplierNoFrom) {
	    this.supplierNoFrom = supplierNoFrom;
	}

	public Integer getSupplierNoTo() {
	    return supplierNoTo;
	}

	public void setSupplierNoTo(Integer supplierNoTo) {
	    this.supplierNoTo = supplierNoTo;
	}

	public String getSupplierName() {
	    return supplierName;
	}

	public void setSupplierName(String supplierName){
	    this.supplierName = supplierName;
	}

	public Integer getPurchaseDivision() {
	    return purchaseDivision;
	}

	public void setPurchaseDivision(Integer purchaseDivision) {
	    this.purchaseDivision = purchaseDivision;
	}

	public String getPrefecture() {
	    return prefecture;
	}

	public void setPrefecture(String prefecture) {
	    this.prefecture = prefecture;
	}

	public String getPhoneNumber() {
	    return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
	    this.phoneNumber = phoneNumber;
	}

	public Integer getItemID(){
	    return itemID;
	}

	public void setItemID(Integer itemID) {
	    this.itemID = itemID;
	}
	
	public void load() {
	    
	    this.clear();
		
	    try {
		
		ConnectionWrapper con = SystemInfo.getConnection();
			
		ResultSetWrapper rs = con.executeQuery(this.getLoadSQL());
			
		while(rs.next()) {
		    MstSupplier ms = new MstSupplier();
		    ms.setData(rs);
		    this.add(ms);
		}
		rs.close();
	    }
	    catch(SQLException e) {
		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }
	}
	
	
	private String getLoadSQL() {
	    
	    StringBuilder sql = new StringBuilder(1000);
	    
	    sql.append(" select distinct\n");
	    sql.append("     ms.*\n");
	    sql.append(" from\n");
	    sql.append("     mst_supplier ms\n");
	    sql.append("         left outer join mst_purchase mp\n");
	    sql.append("                      on mp.supplier_id = ms.supplier_id\n");
	    sql.append("                     and mp.delete_date is null\n");
	    sql.append(" where\n");
	    sql.append("     ms.delete_date is null\n");
	    sql.append(" order by ms.supplier_no ,ms.supplier_name \n");
		
	    return sql.toString();
	}
}

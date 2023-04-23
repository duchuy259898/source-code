/*
 * MstPurchase.java
 *
 * Created on 2007/04/03, 19:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.commodity;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.product.*;


/**
 *
 * @author katagiri
 */
public class MstPurchase extends MstItem
{
	/** Creates a new instance of MstPurchase */
	public MstPurchase()
	{
	}
	
	public MstPurchase(MstSupplier supplier, MstItem item)
	{
		this.setSupplier(supplier);
		this.setData(item);
	}
	
	protected	MstSupplier	supplier		=	null;
	protected	Integer		costPrice		=	0;

	public MstSupplier getSupplier()
	{
		return supplier;
	}

	public void setSupplier(MstSupplier supplier)
	{
		this.supplier = supplier;
	}

	public Integer getCostPrice()
	{
		return costPrice;
	}

	public void setCostPrice(Integer costPrice)
	{
		this.costPrice = costPrice;
	}
	
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		return	(con.executeUpdate(this.getInsertSQL()) == 1);
	}
	
	/**
	 * Insertï∂ÇéÊìæÇ∑ÇÈÅB
	 * @return Insertï∂
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_purchase\n" +
				"values(\n" +
				SQLUtil.convertForSQL(this.getSupplier().getSupplierID()) + ",\n" +
				SQLUtil.convertForSQL(this.getItemID()) + ",\n" +
				SQLUtil.convertForSQL(this.getCostPrice()) + ",\n" +
				"current_timestamp, current_timestamp, null)\n";
	}
}

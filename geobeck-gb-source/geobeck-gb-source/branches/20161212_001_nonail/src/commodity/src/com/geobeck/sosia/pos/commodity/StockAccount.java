/*
 * StockAccount.java
 *
 * Created on 2007/04/18, 9:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.commodity;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.data.commodity.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.commodity.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.util.*;


/**
 *
 * @author katagiri
 */
public class StockAccount extends DataStockAccount
{
	private	Integer		detailSeq		=	0;
	
	private	ArrayList<DataStockAccountDetail>	deleteList	=
			new ArrayList<DataStockAccountDetail>();
	/**
	 * Creates a new instance of StockAccount
	 */
	public StockAccount()
	{
		super();
	}

	public Integer getDetailSeq()
	{
		return detailSeq;
	}
	
	public void addItem(MstPurchase mp)
	{
		DataStockAccountDetail	dsad	=	new DataStockAccountDetail();

		dsad.setGroup(this.getGroup());
		dsad.setShop(this.getShop());
		dsad.setStockNo(this.getStockNo());
		detailSeq	++;
		dsad.setStockDetailNo(detailSeq);
		dsad.setItem(mp);
		dsad.setCostPrice(mp.getCostPrice());
		dsad.setStockNum(1);
		
		this.add(dsad);
	}
	
	public int deleteDetail(Integer stockDetailNo)
	{
//		for(int i = 0; i < this.size(); i ++)
//		{
//			if(this.get(i).getStockDetailNo() == stockDetailNo)
//			{
//				deleteList.add(this.remove(i));
//				return	i;
//			}
//		}
//		
//		return	-1;
		
		this.remove(stockDetailNo);
		return	stockDetailNo;
	}
}

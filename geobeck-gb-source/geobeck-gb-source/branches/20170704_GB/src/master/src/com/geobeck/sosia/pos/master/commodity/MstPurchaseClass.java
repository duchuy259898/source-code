/*
 * MstPurchaseClass.java
 *
 * Created on 2007/04/18, 16:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.commodity;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.product.*;

/**
 *
 * @author katagiri
 */
public class MstPurchaseClass extends ArrayList<MstPurchase>
{
	private	MstItemClass	itemClass	=	new MstItemClass();
	
	/** Creates a new instance of MstPurchaseClass */
	public MstPurchaseClass()
	{
		super();
	}
	
	/**
	 * 文字列に変換する。（商品分類名）
	 * @return 商品分類名
	 */
	public String toString()
	{
		return	this.getItemClassName();
	}

	/**
	 * 商品分類ＩＤを取得する。
	 * @return 商品分類ＩＤ
	 */
	public Integer getItemClassID()
	{
		return itemClass.getItemClassID();
	}

	/**
	 * 商品分類ＩＤをセットする。
	 * @param itemClassID 商品分類ＩＤ
	 */
	public void setItemClassID(Integer itemClassID)
	{
		itemClass.setItemClassID(itemClassID);
	}

	/**
	 * 商品分類名を取得する。
	 * @return 商品分類名
	 */
	public String getItemClassName()
	{
		return itemClass.getItemClassName();
	}

	/**
	 * 商品分類名をセットする。
	 * @param itemClassName 商品分類名
	 */
	public void setItemClassName(String itemClassName)
	{
		itemClass.setItemClassName(itemClassName);
	}

	/**
	 * 表示順を取得する。
	 * @return 表示順
	 */
	public Integer getDisplaySeq()
	{
		return itemClass.getDisplaySeq();
	}

	/**
	 * 表示順をセットする。
	 * @param displaySeq 表示順
	 */
	public void setDisplaySeq(Integer displaySeq)
	{
		itemClass.setDisplaySeq(displaySeq);
	}

	public MstItemClass getItemClass()
	{
		return itemClass;
	}

	public void setItemClass(MstItemClass itemClass)
	{
		this.itemClass = itemClass;
	}
	
}

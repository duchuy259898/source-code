/*
 * MstPurchaseManager.java
 *
 * Created on 2007/04/03, 20:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.commodity;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.master.product.*;
import com.geobeck.sosia.pos.master.commodity.*;

/**
 *
 * @author katagiri
 */
public class MstPurchaseManager
{
	/**
	 * コンストラクタ
	 */
	public MstPurchaseManager(MstSupplier supplier)
	{
		this.setSupplier(supplier);
		itemClasses	=	new MstItemClasses();
		reference	=	new ArrayList<ArrayList<MstPurchase>>();
		selected	=	new ArrayList<ArrayList<MstPurchase>>();
		this.loadItemClasses();
		this.initArrays();
		this.loadPurchases();
	}
	
	/**
	 * 仕入先
	 */
	private MstSupplier			supplier		=	null;
	
	/**
	 * 商品分類
	 */
	private	MstItemClasses						itemClasses		=	null;
	/**
	 * 参照商品
	 */
	private	ArrayList<ArrayList<MstPurchase>>	reference		=	null;
	/**
	 * 選択商品
	 */
	private	ArrayList<ArrayList<MstPurchase>>	selected		=	null;
	
	/**
	 * 選択されている商品分類のインデックス
	 */
	private	Integer				selectedIndex	=	-1;

	/**
	 * 仕入先を取得する。
	 * @return 仕入先
	 */
	public MstSupplier getSupplier()
	{
		return supplier;
	}

	/**
	 * 仕入先を設定する。
	 * @param supplier 仕入先
	 */
	public void setSupplier(MstSupplier supplier)
	{
		this.supplier = supplier;
	}

	/**
	 * 商品分類を取得する。
	 * @return 商品分類
	 */
	public MstItemClasses getItemClasses()
	{
		return itemClasses;
	}

	/**
	 * 商品分類を設定する。
	 * @param itemClasses 商品分類
	 */
	public void setItemClasses(MstItemClasses itemClasses)
	{
		this.itemClasses = itemClasses;
	}

	/**
	 * 参照商品を取得する。
	 * @return 参照商品
	 */
	public ArrayList<ArrayList<MstPurchase>> getReference()
	{
		return reference;
	}

	/**
	 * 参照商品を設定する。
	 * @param reference 参照商品
	 */
	public void setReference(ArrayList<ArrayList<MstPurchase>> reference)
	{
		this.reference = reference;
	}

	/**
	 * 選択商品を取得する。
	 * @return 選択商品
	 */
	public ArrayList<ArrayList<MstPurchase>> getSelected()
	{
		return selected;
	}

	/**
	 * 選択商品を設定する。
	 * @param selected 選択商品
	 */
	public void setSelected(ArrayList<ArrayList<MstPurchase>> selected)
	{
		this.selected = selected;
	}

	/**
	 * 選択されている商品分類のインデックスを取得する。
	 * @return 選択されている商品分類のインデックス
	 */
	public Integer getSelectedIndex()
	{
		return selectedIndex;
	}

	/**
	 * 選択されている商品分類のインデックスを設定する。
	 * @param selectedIndex 選択されている商品分類のインデックス
	 */
	public void setSelectedIndex(Integer selectedIndex)
	{
		this.selectedIndex = selectedIndex;
	}
	
	/**
	 * 商品分類を読み込む。
	 * @return true - 成功、false - 失敗
	 */
	private boolean loadItemClasses()
	{
		boolean		result	=	false;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			itemClasses.load(con);
			
			for(MstItemClass mic : itemClasses)
			{
				mic.loadItem(con);
			}
			
			result	=	true;
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * 配列を初期化する。
	 * @return true - 成功、false - 失敗
	 */
	private boolean initArrays()
	{
		boolean		result	=	false;
		
		reference.clear();
		
		for(MstItemClass mic : itemClasses)
		{
			ArrayList<MstPurchase>	temp	=	new ArrayList<MstPurchase>();
			
			for(MstItem	mi : mic)
			{
				MstPurchase		mp	=	new MstPurchase(this.getSupplier(), mi);
				temp.add(mp);
			}
			
			reference.add(temp);
			selected.add(new ArrayList<MstPurchase>());
		}
		
		return	result;
	}
	
	
	/**
	 * 仕入商品を読み込む。
	 * @return true - 成功、false - 失敗
	 */
	private boolean loadPurchases()
	{
		boolean		result	=	false;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadPurchasSQL());
			
			while(rs.next())
			{
				Integer	itemClassIndex	=	this.getItemClassIndexByID(rs.getInt("item_class_id"));
				Integer	itemIndex		=	this.getItemIndexByID(reference.get(itemClassIndex), rs.getInt("item_id"));
				reference.get(itemClassIndex).get(itemIndex).setCostPrice(rs.getInt("cost_price"));
				this.movePurchase(true, itemClassIndex, itemIndex);
			}
			
			rs.close();
			
			result	=	true;
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * 仕入商品を読み込むＳＱＬ文を取得する。
	 * @return 仕入商品を読み込むＳＱＬ文
	 */
	private String getLoadPurchasSQL()
	{
		return	"select mic.item_class_id, mi.item_id, mp.cost_price\n" +
				"from mst_purchase mp\n" +
				"inner join mst_item mi\n" +
				"on mi.item_id = mp.item_id\n" +
				"and mi.delete_date is null\n" +
				"inner join mst_item_class mic\n" +
				"on mic.item_class_id = mi.item_class_id\n" +
				"and mic.delete_date is null\n" +
				"where mp.delete_date is null\n" +
				"and mp.supplier_id = " + SQLUtil.convertForSQL(this.getSupplier().getSupplierID()) + "\n" +
				"order by mic.item_class_id, mi.item_id\n";
	}
	
	/**
	 * 商品分類ＩＤから商品分類のインデックスを取得する。
	 * @param itemClassID 商品分類ＩＤ
	 * @return 商品分類のインデックス
	 */
	public Integer getItemClassIndexByID(Integer itemClassID)
	{
		for(Integer i = 0; i < itemClasses.size(); i ++)
		{
			if(itemClasses.get(i).getItemClassID() == itemClassID)
			{
				return	i;
			}
		}
		
		return	-1;
	}
	
	/**
	 * 商品ＩＤから商品のインデックスを取得する。
	 * @param itemClass 商品分類
	 * @param itemID 商品ＩＤ
	 * @return 商品のインデックス
	 */
	public Integer getItemIndexByID(ArrayList<MstPurchase> itemClass, Integer itemID)
	{
		for(Integer i = 0; i < itemClass.size(); i ++)
		{
			if(itemClass.get(i).getItemID() == itemID)
			{
				return	i;
			}
		}
		
		return	-1;
	}
	
	/**
	 * 仕入価格を設定する。
	 * @param itemClassIndex 商品分類のインデックス
	 * @param itemIndex 商品のインデックス
	 * @param costPrice 仕入価格
	 */
	public void setCostPrice(Integer itemClassIndex, Integer itemIndex, Integer costPrice)
	{
		if(0 <= itemClassIndex && itemClassIndex < selected.size())
		{
			if(0 <= itemIndex && itemIndex < selected.get(itemClassIndex).size())
			{
				selected.get(itemClassIndex).get(itemIndex).setCostPrice(costPrice);
			}
		}
	}
	
	/**
	 * 商品を選択（解除）する。
	 * @param itemClassIndex 商品分類のインデックス
	 * @param itemIndex 商品のインデックス
	 * @param isSelect true - 選択、false - 解除
	 */
	public void movePurchase(boolean isSelect, Integer itemClassIndex, Integer itemIndex)
	{
		if(itemClassIndex == null || itemIndex == null)
		{
			return;
		}
		
		ArrayList<ArrayList<MstPurchase>>	from	=	(isSelect ? reference : selected);
		ArrayList<ArrayList<MstPurchase>>	to		=	(isSelect ? selected : reference);
		
		ArrayList<MstPurchase>	pcFrom	=	from.get(itemClassIndex);
		ArrayList<MstPurchase>	pcTo	=	to.get(itemClassIndex);
		
		pcTo.add(pcFrom.remove(itemIndex.intValue()));
		
		this.sort(isSelect, itemClassIndex);
	}
	
	/**
	 * 商品を全て選択（解除）する。
	 * @param itemClassIndex 商品分類のインデックス
	 * @param isSelect true - 選択、false - 解除
	 */
	public void moveAllPurchase(boolean isSelect, Integer itemClassIndex)
	{
		if(itemClassIndex == null)
		{
			return;
		}
		
		ArrayList<ArrayList<MstPurchase>>	from	=	(isSelect ? reference : selected);
		ArrayList<ArrayList<MstPurchase>>	to		=	(isSelect ? selected : reference);
		
		ArrayList<MstPurchase>	pcFrom	=	from.get(itemClassIndex);
		ArrayList<MstPurchase>	pcTo	=	to.get(itemClassIndex);
		
		while(0 < pcFrom.size())
		{
			pcTo.add(pcFrom.remove(0));
		}
		
		this.sort(isSelect, itemClassIndex);
	}
	
	/**
	 * ソートを行う。
	 * @param isSelect 
	 * @param itemClassIndex 
	 */
	public void sort(boolean isSelect, Integer itemClassIndex)
	{
		if(itemClassIndex == null)
		{
			return;
		}
		
		ArrayList<ArrayList<MstPurchase>>	mpcs		=	(isSelect ? selected : reference);
		ArrayList<MstPurchase>	mpc	=	mpcs.get(itemClassIndex);
		Collections.sort(mpc, new ProductComparator());
	}
	
	private class ProductComparator implements Comparator<MstPurchase>
	{
		public int compare(MstPurchase mp0, MstPurchase mp1)
		{
			return	mp0.getDisplaySeq().compareTo(mp1.getDisplaySeq());
		}
	}
	
	/**
	 * 登録処理を行う。
	 * @return true - 成功、false - 失敗
	 */
	public boolean regist()
	{
		boolean		result	=	true;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			con.begin();
			
			try
			{
				if(this.deleteData(con))
				{
					for(ArrayList<MstPurchase> mps : selected)
					{
						for(MstPurchase mp : mps)
						{
							if(!mp.regist(con))
							{
								result	=	false;
							}
							
							if(!result)
							{
								break;
							}
						}

						if(!result)
						{
							break;
						}
					}
				}
				else
				{
					result	=	false;
				}
			}
			catch(SQLException e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			
			if(result)
			{
				con.commit();
			}
			else
			{
				con.rollback();
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * 元のデータを削除する。
	 * @param con ConnectionWrapper
	 * @return true - 成功、false - 失敗
	 * @throws java.sql.SQLException SQLException
	 */
	private boolean deleteData(ConnectionWrapper con) throws SQLException
	{
		return (0 <= con.executeUpdate(this.getDeleteMstUseProductSQL()));
	}
	
	/**
	 * 元のデータを削除するＳＱＬ文を取得する。
	 * @return 元のデータを削除するＳＱＬ文
	 */
	private String getDeleteMstUseProductSQL()
	{
		return	"delete from mst_purchase\n" +
				"where supplier_id = " + SQLUtil.convertForSQL(this.getSupplier().getSupplierID()) + "\n";
	}
}

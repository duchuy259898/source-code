/*
 * MstSupplierItemManager.java
 *
 * Created on 2008/09/11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.commodity;

import com.geobeck.sosia.pos.master.commodity.MstSupplier;
import com.geobeck.sosia.pos.master.commodity.MstSupplierItem;
import com.geobeck.sosia.pos.master.product.MstItem;
import com.geobeck.sosia.pos.master.product.MstItemClass;
import com.geobeck.sosia.pos.master.product.MstItemClasses;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;


/**
 *
 * @author mizukawa
 */
public class MstSupplierItemManager
{
	/**
	 * コンストラクタ
	 */
	public MstSupplierItemManager()
	{
		itemClasses	=	new MstItemClasses();
		reference	=	new ArrayList<ArrayList<MstSupplierItem>>();
		selected	=	new ArrayList<ArrayList<MstSupplierItem>>();
		this.loadItemClasses();
		this.initArrays();
		this.loadSupplierItems();
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
	private	ArrayList<ArrayList<MstSupplierItem>>	reference		=	null;
	/**
	 * 選択商品
	 */
	private	ArrayList<ArrayList<MstSupplierItem>>	selected		=	null;
	
	/**
	 * 選択されている商品分類のインデックス
	 */
	private	Integer				selectedIndex	=	-1;
	
	/**
	 * 再読み込み
	 */
	public void reload()
	{
		reference	=	new ArrayList<ArrayList<MstSupplierItem>>();
		selected	=	new ArrayList<ArrayList<MstSupplierItem>>();
		
		this.initArrays();
		this.loadSupplierItems();
	}
	
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
	public ArrayList<ArrayList<MstSupplierItem>> getReference()
	{
		return reference;
	}
	
	/**
	 * 参照商品を設定する。
	 * @param reference 参照商品
	 */
	public void setReference(ArrayList<ArrayList<MstSupplierItem>> reference)
	{
		this.reference = reference;
	}
	
	/**
	 * 選択商品を取得する。
	 * @return 選択商品
	 */
	public ArrayList<ArrayList<MstSupplierItem>> getSelected()
	{
		return selected;
	}
	
	/**
	 * 選択商品を設定する。
	 * @param selected 選択商品
	 */
	public void setSelected(ArrayList<ArrayList<MstSupplierItem>> selected)
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
			ArrayList<MstSupplierItem>	temp	=	new ArrayList<MstSupplierItem>();
			
			for(MstItem	mi : mic)
			{
				MstSupplierItem		msi	=	new MstSupplierItem(null, mi);
				temp.add(msi);
			}
			
			reference.add(temp);
			selected.add(new ArrayList<MstSupplierItem>());
		}
		
		return	result;
	}
	
	
	/**
	 * 仕入商品を読み込む。
	 * @return true - 成功、false - 失敗
	 */
	private boolean loadSupplierItems()
	{
		boolean		result	=	false;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSupplierItemSQL());
			
			while(rs.next())
			{
				int itemClassId = rs.getInt("item_class_id");
				int itemId = rs.getInt("item_id");
				Integer	itemClassIndex	=	this.getItemClassIndexByID(itemClassId);
				Integer	itemIndex		=	this.getItemIndexByID(reference.get(itemClassIndex), itemId);
				Integer supplierId		=	rs.getInt("supplier_id");

				ArrayList<MstSupplierItem> supplierItemArray = reference.get(itemClassIndex);
				MstSupplierItem msi = supplierItemArray.get(itemIndex);
				msi.setCostPrice(rs.getInt("cost_price"));
				this.selectSupplierItem(supplierId, itemClassIndex, itemIndex);
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
	private String getLoadSupplierItemSQL()
	{
		return	"select mic.item_class_id, mi.item_id, msi.supplier_id, msi.cost_price\n" +
			"from mst_supplier_item msi\n" +
			"inner join mst_item mi\n" +
			"on mi.item_id = msi.item_id\n" +
			"and mi.delete_date is null\n" +
			"inner join mst_item_class mic\n" +
			"on mic.item_class_id = mi.item_class_id\n" +
			"and mic.delete_date is null\n" +
			"where msi.delete_date is null\n" +
//				"and msi.supplier_id = " + SQLUtil.convertForSQL(this.getSupplier().getSupplierID()) + "\n" +
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
			Integer ici = itemClasses.get(i).getItemClassID();
			if(itemClassID.equals(ici))
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
	public Integer getItemIndexByID(ArrayList<MstSupplierItem> itemClass, Integer itemID)
	{
		for(Integer i = 0; i < itemClass.size(); i ++)
		{
			if(itemClass.get(i).getItemID().equals(itemID))
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
	public void moveSupplierItem(boolean isSelect, Integer itemClassIndex, Integer itemIndex)
	{
		if(itemClassIndex == null || itemIndex == null)
		{
			return;
		}
		
		ArrayList<ArrayList<MstSupplierItem>>	from		=	(isSelect ? reference : selected);
		ArrayList<ArrayList<MstSupplierItem>>	to			=	(isSelect ? selected : reference);
		MstSupplier								supplier	=	(isSelect ? null : this.supplier);
		
		ArrayList<MstSupplierItem>	pcFrom	=	from.get(itemClassIndex);
		ArrayList<MstSupplierItem>	pcTo	=	to.get(itemClassIndex);
		
		int idx = MstSupplierItemManager.convertIndex(pcFrom, supplier, itemIndex);
		MstSupplierItem item = pcFrom.remove(idx);
		pcTo.add(item);
		
		if (isSelect)
		{
			item.setSupplier(this.getSupplier());
		}
		else
		{
			item.setSupplier(null);
		}
		
		this.sort(isSelect, itemClassIndex);
	}
	
	/**
	 * 商品を選択する。
	 * @param supplierID 仕入先ID
	 * @param itemClassIndex 商品分類のインデックス
	 * @param itemIndex 商品のインデックス
	 */
	public void selectSupplierItem(int supplierID, Integer itemClassIndex, Integer itemIndex)
	{
		if(itemClassIndex == null || itemIndex == null)
		{
			return;
		}
		
		ArrayList<ArrayList<MstSupplierItem>>	from		=	reference;
		ArrayList<ArrayList<MstSupplierItem>>	to			=	selected;
		
		ArrayList<MstSupplierItem>	pcFrom	=	from.get(itemClassIndex);
		ArrayList<MstSupplierItem>	pcTo	=	to.get(itemClassIndex);
		
		MstSupplierItem item = pcFrom.remove(itemIndex.intValue());
		pcTo.add(item);
		
		MstSupplier supplier = new MstSupplier();
		supplier.setSupplierID(supplierID);
		item.setSupplier(supplier);
		
		this.sort(true, itemClassIndex);
	}
	
	/**
	 * 商品を全て選択（解除）する。
	 * @param itemClassIndex 商品分類のインデックス
	 * @param isSelect true - 選択、false - 解除
	 */
	public void moveAllSupplierItem(boolean isSelect, Integer itemClassIndex)
	{
		if(itemClassIndex == null || itemClassIndex < 0)
		{
			return;
		}
		
		ArrayList<ArrayList<MstSupplierItem>>	from	=	(isSelect ? reference : selected);
		ArrayList<ArrayList<MstSupplierItem>>	to		=	(isSelect ? selected : reference);
		
		ArrayList<MstSupplierItem>	pcFrom	=	from.get(itemClassIndex);
		ArrayList<MstSupplierItem>	pcTo	=	to.get(itemClassIndex);
		
		for (int i = pcFrom.size() - 1; i >= 0; i--)
		{
			MstSupplierItem item = pcFrom.get(i);
			if (isSelect)
			{
				item.setSupplier(this.getSupplier());
			}
			else
			{
				if (item.getSupplier() != null && !item.getSupplier().getSupplierID().equals(this.getSupplier().getSupplierID()))
				{
					continue;
				}
				item.setSupplier(null);
			}
			
			pcFrom.remove(i);
			pcTo.add(item);
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
		if(itemClassIndex == null || itemClassIndex < 0)
		{
			return;
		}
		
		ArrayList<ArrayList<MstSupplierItem>>	mpcs		=	(isSelect ? selected : reference);
		ArrayList<MstSupplierItem>	mpc	=	mpcs.get(itemClassIndex);
		Collections.sort(mpc, new ProductComparator());
	}

	private class ProductComparator implements Comparator<MstSupplierItem>
	{
		public int compare(MstSupplierItem mp0, MstSupplierItem mp1)
		{
			return	mp0.getDisplaySeq().compareTo(mp1.getDisplaySeq());
		}
	}
	
	/**
	 * 登録処理を行う。
	 * @return true - 成功、false - 失敗
	 */
	public boolean register(ConnectionWrapper con) throws SQLException
	{
		try
		{
			if(this.deleteData(con))
			{
				for(ArrayList<MstSupplierItem> msis : selected)
				{
					for(MstSupplierItem msi : msis)
					{
						if (msi.getSupplier().getSupplierID() == null)
						{
							msi.setSupplier(this.getSupplier());
						}
						else if (this.getSupplier() != null && !this.getSupplier().getSupplierID().equals(msi.getSupplier().getSupplierID()))
						{
							continue;
						}
						
						if(!msi.regist(con))
						{
							return false;
						}
					}
				}
			}
			else
			{
				return false;
			}
			
			return true;
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw e;
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
				if (register(con))
				{
					con.rollback();
					return false;
				}
				
				con.commit();
				return true;
			}
			catch(SQLException e)
			{
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
        public boolean checkExistItemRegisted() {
            ConnectionWrapper	con	=	SystemInfo.getConnection();
            try {
                ResultSetWrapper	rs	=	con.executeQuery(this.getCheckExistItemRegistedSQL());
                if(rs.next()) return true;
            }catch (Exception e) {
                
            }
            
            return false;
        }
        private String getCheckExistItemRegistedSQL() {
            return	"select 1 from mst_supplier_item \n"
			+ " where delete_date is null  and supplier_id = " + SQLUtil.convertForSQL(this.getSupplier().getSupplierID()) + "\n"; 
        }
	/**
	 * 仕入先削除時に呼ぶ
	 */
	public boolean delete()
	{
		boolean		result	=	true;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			con.begin();
			try
			{
				if (this.updateSql(con, this.getDeleteBySupplierSQL()))
				{
					con.commit();
					return true;
				}
				
				con.rollback();
				return false;
			}
			catch(SQLException e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
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
		return updateSql(con, this.getDeleteBySupplierSQL());
	}
	
	/**
	 * 汎用SQL実行メソッド
	 */
	private boolean updateSql(ConnectionWrapper con, String sql) throws SQLException
	{
		return (0 <= con.executeUpdate(sql));
	}
	
	/**
	 * 元のデータを削除するＳＱＬ文を取得する。
	 * @return 元のデータを削除するＳＱＬ文
	 */
	private String getLogicalDeleteSQL()
	{
		return	"delete from mst_supplier_item\n"
			+ "where supplier_id = " + SQLUtil.convertForSQL(this.getSupplier().getSupplierID()) + "\n";
	}
	
	/**
	 * 仕入先削除時に発行するSQL
	 */
	private String getDeleteBySupplierSQL()
	{
		return	"update mst_supplier_item set\n"
			+ "delete_date = current_timestamp\n"
			+ "where supplier_id = " + SQLUtil.convertForSQL(this.getSupplier().getSupplierID()) + "\n";
	}
	
	/**
	 * listに格納されているMstSupplierItemの中で、supplierに一致するindex番目のMstSupplierItemを返す
	 * @param list MstSupplierItemが格納されたリスト
	 * @param supplier 仕入先
	 * @param index 商品の中で指定された仕入先のもののインデックス
	 * @return listでのインデックス
	 */
	public static int convertIndex(List<MstSupplierItem> list, MstSupplier supplier, int index)
	{
            int cnt = -1;
            int size = list.size();

            for (int i = 0; i < size; i++) {
                MstSupplierItem msi = list.get(i);
                if (supplier != null && !supplier.getSupplierID().equals(msi.getSupplier().getSupplierID())) {
                    continue;
                }

                cnt++;
                if (cnt == index) {
                    return i;
                }
            }

            return -1;
	}
}
